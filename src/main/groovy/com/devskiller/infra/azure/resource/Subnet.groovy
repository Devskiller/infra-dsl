package com.devskiller.infra.azure.resource

import com.devskiller.infra.azure.internal.InfrastructureElement
import com.devskiller.infra.azure.ResourceGroup

class Subnet extends InfrastructureElement {

	int id

	protected Subnet(ResourceGroup resourceGroup, String name) {
		super(resourceGroup, 'azurerm_subnet', name)
	}

	void subnetId(int id) {
		this.id = id
	}

	Map getAsMap() {
		[:]
	}
}
