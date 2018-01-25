package com.devskiller.infra.azure

import com.devskiller.infra.azure.internal.InfrastructureElement

abstract class ComponentElement extends InfrastructureElement {

	protected ComponentElement(ResourceGroup resourceGroup, String resourceType, String componentName) {
		super(resourceGroup, resourceType, componentName)
	}
}
