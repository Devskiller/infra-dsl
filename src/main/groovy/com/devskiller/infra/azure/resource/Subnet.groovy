package com.devskiller.infra.azure.resource

import com.devskiller.infra.azure.ResourceGroup
import com.devskiller.infra.azure.internal.InfrastructureElement
import com.devskiller.infra.util.Cidr

class Subnet extends InfrastructureElement {

	private int subnetId
	private final String networkCidr

	protected Subnet(ResourceGroup resourceGroup, String name, String networkCidr) {
		super(resourceGroup, 'azurerm_subnet', name)
		this.networkCidr = networkCidr
	}

	void subnetId(int id) {
		this.subnetId = id
	}

	Map getAsMap() {
		['address_space':new Cidr(networkCidr).getSubnetCidr(subnetId)]
	}
}
