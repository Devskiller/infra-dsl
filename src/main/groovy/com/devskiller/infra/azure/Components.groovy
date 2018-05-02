package com.devskiller.infra.azure

import com.devskiller.infra.internal.DslContext
import com.devskiller.infra.internal.InfrastructureElementCollection
import com.devskiller.infra.internal.ResourceGroup

class Components extends InfrastructureElementCollection {

	List<Component> entries = []

	protected Components(ResourceGroup resourceGroup) {
		super(resourceGroup)
	}

	void component(String name, @DelegatesTo(Component) Closure closure) {
		entries << DslContext.create(new Component(resourceGroup, name), closure)
	}

}
