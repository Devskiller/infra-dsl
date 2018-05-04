package com.devskiller.infra.internal

import javaposse.jobdsl.dsl.NoDoc

import com.devskiller.infra.hcl.HclMarshaller
import com.devskiller.infra.hcl.HclUtil

abstract class InfrastructureElement {

	protected final ResourceGroup resourceGroup

	private final String resourceType

	private final String componentName

	protected String elementName

	protected InfrastructureElement(ResourceGroup resourceGroup, String resourceType, String componentName = null) {
		this.resourceGroup = resourceGroup
		this.resourceType = resourceType
		this.componentName = componentName
	}

	protected void setElementName(String elementName) {
		this.elementName = elementName
	}

	@NoDoc
	protected abstract Map getAsMap()

	@NoDoc
	String renderElement() {
		HclMarshaller.resource(resourceType, elementName(), elementProperties())
	}

	@NoDoc
	String renderDataElement() {
		HclMarshaller.data(resourceType, elementName(), resourceGroup.commonProperties(componentName, elementName()))
	}

	@NoDoc
	Map elementProperties(boolean includeLocation = true, boolean includeTags = true) {
		Map<String, String> properties = resourceGroup.commonProperties(componentName, elementName())
		if (includeLocation) {
			properties << resourceGroup.location()
		}
		properties << getAsMap()
		if (includeTags) {
			properties << resourceGroup.getCommonTags(componentName, elementName())
		}
		return properties
	}

	@NoDoc
	String dataSourceElementId(boolean external = false) {
		String prefix = external ? 'data.' : ''
		return "\${$prefix$resourceType.${HclUtil.escapeResourceName(elementName())}.id}"
	}

	String elementName() {
		List<String> names = []
		if (componentName) {
			names << componentName
		}
		if (elementName) {
			names << elementName
		}
		resourceGroup.getResourceQualifier(this.class, names)
	}

	@NoDoc
	void call(Closure closure) {
		closure.delegate = this
		closure.call()
	}

}
