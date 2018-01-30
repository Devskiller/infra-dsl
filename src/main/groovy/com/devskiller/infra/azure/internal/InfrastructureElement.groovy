package com.devskiller.infra.azure.internal

import com.devskiller.infra.azure.ResourceGroup
import com.devskiller.infra.hcl.HclMarshaller

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

	Map elementProperties() {
		commonProperties() + getAsMap() + resourceGroup.getCommonTags(componentName)
	}

	private Map<String, String> commonProperties() {
		[
				'name'               : elementName(),
				'resource_group_name': resourceGroup.getResourceQualifier(ResourceGroup.class, []),
				'location'           : resourceGroup.region
		]
	}

	String dataSourceElementId() {
		return "\${$resourceType.${elementName()}.id}"
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
