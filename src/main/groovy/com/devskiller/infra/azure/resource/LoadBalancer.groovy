package com.devskiller.infra.azure.resource

import com.devskiller.infra.azure.ResourceGroup
import com.devskiller.infra.azure.internal.InfrastructureElement

class LoadBalancer extends InfrastructureElement {

	private final String name
	private final BackendPool backendPool

	private PublicIp publicIp
	private IpAllocationMethod privateIpAllocation = IpAllocationMethod.Dynamic

	LoadBalancer(ResourceGroup resourceGroup, String name, PublicIp publicIp) {
		super(resourceGroup, 'azure_lb', name)
		this.backendPool = new BackendPool(resourceGroup, name)
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

	@Override
	String renderElement() {
		return super.renderElement() + backendPool.renderElement()
	}

	class BackendPool extends InfrastructureElement {

		protected BackendPool(ResourceGroup resourceGroup, String name) {
			super(resourceGroup, 'azurerm_lb_backend_address_pool', name)
		}

		@Override
		protected Map getAsMap() {
			[
					'loadbalancer_id': LoadBalancer.this.dataSourceElementName()
			]
		}
	}
}
