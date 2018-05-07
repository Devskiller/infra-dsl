package com.devskiller.infra.aws.resource

import com.devskiller.infra.internal.DslContext
import com.devskiller.infra.internal.InfrastructureElement
import com.devskiller.infra.internal.InfrastructureElementCollection
import com.devskiller.infra.internal.ResourceGroup
import com.devskiller.infra.util.NameUtils

class Vpc extends InfrastructureElement {

	int networkId = 1
	String cidr
	SubnetList subnetList

	Vpc(ResourceGroup resourceGroup) {
		super(resourceGroup, 'aws_vpc')
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
				'cidr_block': networkCidr
		]
	}

	@Override
	String renderElement() {
		NameUtils.concatenateElements('\n',
				[
						super?.renderElement(),
						subnetList?.renderElement()
				].findAll({ it != null }))
	}

	class SubnetList extends InfrastructureElementCollection {

		final List<Subnet> entries = []
		private final String networkCidr

		protected SubnetList(ResourceGroup resourceGroup, String networkCidr) {
			super(resourceGroup)
			this.networkCidr = networkCidr
		}

		void subnet(String name, @DelegatesTo(Subnet) Closure closure) {
			Subnet subnet = new Subnet(resourceGroup, name, networkCidr, Vpc.this.dataSourceElementId())
			entries << DslContext.create(subnet, closure)
		}
	}

}
