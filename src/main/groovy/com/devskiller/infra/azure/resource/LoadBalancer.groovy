package com.devskiller.infra.azure.resource

import com.devskiller.infra.azure.ResourceGroup
import com.devskiller.infra.azure.internal.InfrastructureElement

class LoadBalancer extends InfrastructureElement {

	private final String name

	private PublicIp publicIp
	private IpAllocationMethod privateIpAllocation = IpAllocationMethod.Dynamic

	LoadBalancer(ResourceGroup resourceGroup, String name, PublicIp publicIp) {
		super(resourceGroup, 'azure_lb', name)
		this.name = name
		this.publicIp = publicIp
	}

	void privateIpAllocation(IpAllocationMethod privateIpAllocation) {
		this.privateIpAllocation = privateIpAllocation
	}

	@Override
	protected Map getAsMap() {
		Map frontConfig = [
				'name': resourceGroup.getLoadBalancerFrontedConfigName(name)
		]

		if (publicIp) {
			frontConfig << ['public_ip_address_id': publicIp.dataSourceElementName()]
		} else {
			frontConfig << ['private_ip_address_allocation': privateIpAllocation.name()]
		}

		return [
				'frontend_ip_configuration': frontConfig
		]
	}
}
