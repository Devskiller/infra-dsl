package com.devskiller.infra.azure.resource

import com.devskiller.infra.azure.internal.DslContext
import com.devskiller.infra.azure.internal.InfrastructureElementCollection
import com.devskiller.infra.azure.ResourceGroup

class SubnetList extends InfrastructureElementCollection {

	final List<Subnet> entries = []
	private final String networkCidr

	protected SubnetList(ResourceGroup resourceGroup, String networkCidr) {
		super(resourceGroup)
		this.networkCidr = networkCidr
	}

	void subnet(String name, @DelegatesTo(Subnet) Closure closure) {
		Subnet subnet = new Subnet(resourceGroup, name, networkCidr)
		entries << DslContext.create(subnet, closure)
	}
}
