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
		HclMarshaller.data(resourceType, elementName(), commonProperties())
	}

	@NoDoc
	Map elementProperties(boolean includeLocation = true, boolean includeTags = true) {
		Map<String, String> properties = commonProperties()
		if (includeLocation) {
			properties << location()
		}
		properties << getAsMap()
		if (includeTags) {
			properties << resourceGroup.getCommonTags(componentName)
		}
		return properties
	}

	@NoDoc
	Map<String, String> commonProperties() {
		[
				'name'               : elementName(),
				'resource_group_name': resourceGroup.getResourceQualifier(ResourceGroup.class, [])
		]
	}

	@NoDoc
	Map<String, String> location() {
		[
				'location'           : resourceGroup.region
		]
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
