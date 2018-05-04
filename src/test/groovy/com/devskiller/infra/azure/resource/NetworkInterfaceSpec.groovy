package com.devskiller.infra.azure.resource

class NetworkInterfaceSpec extends AzureResourceGroupAwareSpec {

	def "should render default"() {
		given:
			NetworkInterface networkInterface = new NetworkInterface(resourceGroup(), 'db',
					null, null)
			networkInterface.setElementName('1')
			networkInterface.subnetName('app')
		when:
			Map properties = networkInterface.elementProperties()
		then:
			properties
			properties.get('name') == 'test-weu-ni-db-1'
			properties.get('enable_accelerated_networking') == false
		and:
			Map ipConfiguration = properties.get('ip_configuration')
			ipConfiguration.get('subnet_id') == '${azurerm_subnet.test-weu-subnet-app.id}'
			ipConfiguration.get('private_ip_address_allocation') == 'Dynamic'
	}

	def "should render customized"() {
		given:
			LoadBalancer loadBalancer = new LoadBalancer(resourceGroup(), 'db', null)
			loadBalancer.natRule {
				name 'ssh-2'
			}
			loadBalancer.natRule {
				name 'mysql-2'
			}
			NetworkInterface networkInterface = new NetworkInterface(resourceGroup(), 'db',
					new NetworkSecurityGroup(resourceGroup(), 'db'),
					loadBalancer)
			networkInterface.setElementName('2')
			networkInterface.privateIpAllocation(IpAllocationMethod.Static)
			networkInterface.subnetName('app')
			networkInterface.enableAcceleratedNetworking(true)
		when:
			Map properties = networkInterface.elementProperties()
		then:
			println properties
			properties
			properties.get('name') == 'test-weu-ni-db-2'
			properties.get('network_security_group_id') == '${azurerm_network_security_group.test-weu-nsg-db.id}'
			properties.get('enable_accelerated_networking') == true
		and:
			Map ipConfiguration = properties.get('ip_configuration')
			ipConfiguration.get('subnet_id') == '${azurerm_subnet.test-weu-subnet-app.id}'
			ipConfiguration.get('private_ip_address_allocation') == 'Static'
			ipConfiguration.get('load_balancer_backend_address_pools_ids') == ['${azurerm_lb_backend_address_pool.test-weu-bap-db.id}']
			ipConfiguration.get('load_balancer_inbound_nat_rules_ids') == ['${azurerm_lb_nat_rule.test-weu-lbnr-db-ssh-2.id}', '${azurerm_lb_nat_rule.test-weu-lbnr-db-mysql-2.id}']

	}

}
