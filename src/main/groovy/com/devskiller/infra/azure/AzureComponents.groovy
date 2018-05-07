package com.devskiller.infra.azure

import com.devskiller.infra.internal.DslContext
import com.devskiller.infra.internal.InfrastructureElementCollection
import com.devskiller.infra.internal.ResourceGroup

class AzureComponents extends InfrastructureElementCollection {

	List<AzureComponent> entries = []

	protected AzureComponents(ResourceGroup resourceGroup) {
		super(resourceGroup)
	}

	void component(String name, @DelegatesTo(AzureComponent) Closure closure) {
		entries << DslContext.create(new AzureComponent(resourceGroup, name), closure)
	}

}
