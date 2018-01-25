package com.devskiller.infra.azure

import com.devskiller.infra.azure.internal.DslContext
import com.devskiller.infra.azure.internal.InfrastructureElementCollection

class Components extends InfrastructureElementCollection {

	List<Component> entries = []

	protected Components(ResourceGroup resourceGroup) {
		super(resourceGroup)
	}

	void component(String name, @DelegatesTo(Component) Closure closure) {
		entries << DslContext.create(new Component(resourceGroup, name), closure)
	}

}
