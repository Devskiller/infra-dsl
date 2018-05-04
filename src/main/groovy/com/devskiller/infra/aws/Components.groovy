package com.devskiller.infra.aws

import com.devskiller.infra.internal.DslContext
import com.devskiller.infra.internal.InfrastructureElementCollection
import com.devskiller.infra.internal.ResourceGroup

class Components extends InfrastructureElementCollection {

	List<Component> entries = []
	private final String vpcElementId

	protected Components(ResourceGroup resourceGroup, String vpcElementId) {
		super(resourceGroup)
		this.vpcElementId = vpcElementId
	}

	void component(String name, @DelegatesTo(Component) Closure closure) {
		entries << DslContext.create(new Component(resourceGroup, name, vpcElementId), closure)
	}

}
