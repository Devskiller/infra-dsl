package com.devskiller.infra.azure.resource

import com.devskiller.infra.azure.internal.DslContext
import com.devskiller.infra.azure.internal.InfrastructureElement
import com.devskiller.infra.azure.ResourceGroup

class Network extends InfrastructureElement {

	int networkId = 1
	String cidr
	SubnetList subnetList

	Network(ResourceGroup resourceGroup) {
		super(resourceGroup, 'azurerm_virtual_network')
	}

	void networkId(int networkId) {
		this.networkId = networkId
	}

	void cidr(String cidr) {
		this.cidr = cidr
	}

	void subnets(@DelegatesTo(SubnetList) Closure closure) {
		subnetList = DslContext.create(new SubnetList(resourceGroup, getNetworkCidr()), closure)
	}

	String getNetworkCidr() {
		return cidr ?: String.format("10.%d.0.0/16", networkId)
	}

	@Override
	Map getAsMap() {
		return [
				'address_space': networkCidr
		]
	}

	@Override
	String renderElement() {
		super.renderElement() + subnetList?.renderElement()
	}

}
