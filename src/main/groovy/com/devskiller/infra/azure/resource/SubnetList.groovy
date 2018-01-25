package com.devskiller.infra.azure.resource

import com.devskiller.infra.azure.internal.InfrastructureElementCollection
import com.devskiller.infra.azure.ResourceGroup

class SubnetList extends InfrastructureElementCollection {

	List<Subnet> entries = []

	protected SubnetList(ResourceGroup resourceGroup) {
		super(resourceGroup)
	}

	void subnet(String name, @DelegatesTo(Subnet) Closure closure) {
		Subnet subnet = new Subnet(resourceGroup, name)
		closure.delegate = subnet
		closure()
		entries << subnet
	}
}
