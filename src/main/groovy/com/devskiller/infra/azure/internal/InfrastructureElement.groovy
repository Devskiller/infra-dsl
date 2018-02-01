package com.devskiller.infra.azure.internal

import com.devskiller.infra.azure.ResourceGroup
import com.devskiller.infra.hcl.HclMarshaller
import com.devskiller.infra.hcl.HclUtil

abstract class InfrastructureElement {

	protected final ResourceGroup resourceGroup

	private final String resourceType

	private final String componentName

	private String elementName

	protected InfrastructureElement(ResourceGroup resourceGroup, String resourceType, String componentName = null) {
		this.resourceGroup = resourceGroup
		this.resourceType = resourceType
		this.componentName = componentName
	}

	protected void setElementName(String elementName) {
		this.elementName = elementName
	}

	protected abstract Map getAsMap()

	String renderElement() {
		HclMarshaller.resource(resourceType,
				elementName(),
				elementProperties())
	}

	Map elementProperties(boolean includeLocation = true, boolean  includeTags = true) {
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

	Map<String, String> commonProperties() {
		[
				'name'               : elementName(),
				'resource_group_name': resourceGroup.getResourceQualifier(ResourceGroup.class, [])
		]
	}

	Map<String, String> location() {
		[
				'location'           : resourceGroup.region
		]
	}

	String dataSourceElementId() {
		return "\${$resourceType.${HclUtil.escapeResourceName(elementName())}.id}"
	}

	protected String elementName() {
		List<String> names = []
		if (componentName) {
			names << componentName
		}
		if (elementName) {
			names << elementName
		}
		resourceGroup.getResourceQualifier(this.class, names)
	}

	void call(Closure closure) {
		closure.delegate = this
		closure.call()
	}

}
