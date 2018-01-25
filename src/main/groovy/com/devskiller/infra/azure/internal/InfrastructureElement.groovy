package com.devskiller.infra.azure.internal

import com.devskiller.infra.azure.ResourceGroup
import com.devskiller.infra.hcl.HclMarshaller

abstract class InfrastructureElement {

	protected final ResourceGroup resourceGroup

	private final String resourceType

	private String[] names

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

	Map<String, String> commonProperties() {
		[
				'name'               : elementName(),
				'resource_group_name': resourceGroup.getResourceQualifier(ResourceGroup.class),
				'location'           : resourceGroup.region
		]
	}

	protected abstract Map getAsMap()

	String renderElement() {
		HclMarshaller.resource(resourceType,
				elementName(),
				resourceGroup.wrapWithTags(commonProperties() + getAsMap()))
	}

	protected String elementName() {
		resourceGroup.getResourceQualifier(this.class, names)
	}

}
