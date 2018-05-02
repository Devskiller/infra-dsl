package com.devskiller.infra.azure.resource

import com.devskiller.infra.internal.DslContext
import com.devskiller.infra.internal.InfrastructureElement
import com.devskiller.infra.internal.ResourceGroup
import com.devskiller.infra.internal.InfrastructureElementCollection

class Network extends InfrastructureElement {

	int networkId = 1
	String cidr
	SubnetList subnetList
	PeeringList peeringList

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

	void peerings(@DelegatesTo(PeeringList) Closure closure) {
		peeringList = DslContext.create(new PeeringList(resourceGroup), closure)
	}

	String getNetworkCidr() {
		return cidr ?: String.format("10.%d.0.0/16", networkId)
	}

	@Override
	Map getAsMap() {
		return [
				'address_space': [networkCidr]
		]
	}

	@Override
	String renderElement() {
		super.renderElement() + subnetList?.renderElement() + peeringList?.renderElement()
	}

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

	class PeeringList extends InfrastructureElementCollection {
		final List<NetworkPeering> entries = []

		protected PeeringList(ResourceGroup resourceGroup) {
			super(resourceGroup)
		}

		void peering(@DelegatesTo(NetworkPeering) Closure closure) {
			entries << DslContext.create(new NetworkPeering(resourceGroup, Network.this), closure)
		}
	}

}
