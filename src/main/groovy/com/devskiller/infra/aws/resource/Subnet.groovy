package com.devskiller.infra.aws.resource

import com.devskiller.infra.internal.InfrastructureElement
import com.devskiller.infra.internal.ResourceGroup
import com.devskiller.infra.util.Cidr

class Subnet extends InfrastructureElement {

	private int subnetId
	private final String networkCidr
	private final String vpcElementId

	protected Subnet(ResourceGroup resourceGroup, String name, String networkCidr, String vpcElementId) {
		super(resourceGroup, 'aws_subnet', name)
		this.vpcElementId = vpcElementId
		this.networkCidr = networkCidr
	}

	void subnetId(int id) {
		this.subnetId = id
	}

	@Override
	Map elementProperties() {
		elementProperties(false, true)
	}

	@Override
	Map getAsMap() {
		[
				'vpc_id'    : vpcElementId,
				'cidr_block': new Cidr(networkCidr).getSubnetCidr(subnetId)
		]
	}
}
