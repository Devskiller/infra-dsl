package com.devskiller.infra.azure.internal

import com.devskiller.infra.azure.ResourceGroup
import com.devskiller.infra.hcl.HclMarshaller

abstract class InfrastructureElement {

	protected final ResourceGroup resourceGroup

	private final String resourceType

	private final String[] names

	protected InfrastructureElement(ResourceGroup resourceGroup, String resourceType) {
		this.resourceGroup = resourceGroup
		this.resourceType = resourceType
		this.names = []
	}

	protected InfrastructureElement(ResourceGroup resourceGroup, String resourceType, String name) {
		this.resourceGroup = resourceGroup
		this.resourceType = resourceType
		this.names = [name]
	}

	protected abstract Map getAsMap()

	String renderElement() {
		HclMarshaller.resource(resourceType,
				elementName(),
				elementProperties())
	}

	Map elementProperties() {
		resourceGroup.wrapWithTags(commonProperties() + getAsMap())
	}

	private Map<String, String> commonProperties() {
		[
				'name'               : elementName(),
				'resource_group_name': resourceGroup.getResourceQualifier(ResourceGroup.class),
				'location'           : resourceGroup.region
		]
	}

	private String elementName() {
		resourceGroup.getResourceQualifier(this.class, names)
	}

}
