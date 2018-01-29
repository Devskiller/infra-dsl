package com.devskiller.infra.azure.resource

import com.devskiller.infra.azure.ResourceGroup
import com.devskiller.infra.azure.internal.InfrastructureElement

class NetworkInterface extends InfrastructureElement {

	private final String componentName
	private final NetworkSecurityGroup networkSecurityGroup

	private final IpConfiguration ipConfiguration = new IpConfiguration()

	private boolean enableAcceleratedNetworking = false

	protected NetworkInterface(ResourceGroup resourceGroup, String componentName,
	                           NetworkSecurityGroup networkSecurityGroup,
	                           LoadBalancer loadBalancer) {
		super(resourceGroup, 'azurerm_network_interface', componentName)
		this.componentName = componentName
		this.networkSecurityGroup = networkSecurityGroup
		this.ipConfiguration.loadBalancer = loadBalancer
	}

	void subnetName(String subnetName) {
		this.ipConfiguration.subnetName = subnetName
	}

	void privateIpAllocation(IpAllocationMethod privateIpAllocation) {
		this.ipConfiguration.privateIpAllocation = privateIpAllocation
	}

	void enableAcceleratedNetworking(boolean enableAcceleratedNetworking) {
		this.enableAcceleratedNetworking = enableAcceleratedNetworking
	}

	@Override
	protected Map getAsMap() {
		Map ipConfig = ['enable_accelerated_networking': enableAcceleratedNetworking]

		if (networkSecurityGroup) {
			ipConfig << ['network_security_group_id': networkSecurityGroup.dataSourceElementId()]
		}

		ipConfig << ipConfiguration.getAsMap()

		return ipConfig
	}

	class IpConfiguration {

		private IpAllocationMethod privateIpAllocation = IpAllocationMethod.Dynamic
		private String subnetName
		private LoadBalancer loadBalancer

		protected Map getAsMap() {
			Map frontConfig = [
					'name'                         : name(),
					'subnet_id'                    : new Subnet(NetworkInterface.this.resourceGroup, subnetName, null).dataSourceElementId(),
					'private_ip_address_allocation': privateIpAllocation.name()
			]

			if (loadBalancer) {
				frontConfig << ['load_balancer_backend_address_pools_ids': [loadBalancer.getBackendPool().dataSourceElementId()]]

				if (loadBalancer.getNatRules()) {
					List<String> natRules = loadBalancer.getNatRules().collect {
						natRule ->
							natRule.dataSourceElementId()
					}
					frontConfig << ['load_balancer_inbound_nat_rules_ids': natRules]
				}
			}

			return [
					'ip_configuration': frontConfig
			]
		}

		private String name() {
			NetworkInterface.this.resourceGroup.getResourceQualifier(this.class, [componentName])
		}
	}
}
