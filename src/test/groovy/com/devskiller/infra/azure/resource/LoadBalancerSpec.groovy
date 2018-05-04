package com.devskiller.infra.azure.resource

class LoadBalancerSpec extends AzureResourceGroupAwareSpec {

	def "should render load balancer"() {
		given:
			LoadBalancer loadBalancer = new LoadBalancer(resourceGroup(), 'db', null)
			loadBalancer.privateIpAllocation(IpAllocationMethod.Static)
		when:
			Map properties = loadBalancer.elementProperties()
		then:
			properties.get('name') == 'test-weu-lb-db'
			properties.get('frontend_ip_configuration') == [
					'name'                         : 'test-weu-fipc-db',
					'private_ip_address_allocation': 'Static'
			]
	}

	def "should render probe"() {
		given:
			LoadBalancer loadBalancer = new LoadBalancer(resourceGroup(), 'front', null)
			LoadBalancer.Probe probe = new LoadBalancer.Probe(loadBalancer, resourceGroup(), 'front')
			probe.name('www')
			probe.port(443)
			probe.protocol(LoadBalancer.ProbeProtocol.Http)
		when:
			Map properties = probe.elementProperties()
		then:
			properties.get('name') == 'test-weu-probe-front-www'
			properties.get('loadbalancer_id') == '${azurerm_lb.test-weu-lb-front.id}'
			properties.get('port') == 443
			properties.get('protocol') == LoadBalancer.ProbeProtocol.Http
	}

	def "should render rule"() {
		given:
			LoadBalancer loadBalancer = new LoadBalancer(resourceGroup(), 'front', null)
			loadBalancer.probe {
				name 'www'
			}
			LoadBalancer.Rule rule = new LoadBalancer.Rule(loadBalancer, resourceGroup(), 'front')
			rule.name('http')
			rule.probeName('www')
			rule.protocol(LoadBalancer.TransportProtocol.Tcp)
			rule.loadDistribution(LoadBalancer.LoadDistribution.SourceIP)
			rule.enableFloatingIp(true)
		when:
			Map properties = rule.elementProperties()
		then:
			properties.get('name') == 'test-weu-lbr-front-http'
			properties.get('loadbalancer_id') == '${azurerm_lb.test-weu-lb-front.id}'
			properties.get('frontend_ip_configuration_name') == 'test-weu-fipc-front'
			properties.get('probe_id') == '${azurerm_lb_probe.test-weu-probe-front-www.id}'
			properties.get('protocol') == LoadBalancer.TransportProtocol.Tcp
			properties.get('load_distribution') == LoadBalancer.LoadDistribution.SourceIP
			properties.get('backend_address_pool_id') == '${azurerm_lb_backend_address_pool.test-weu-bap-front.id}'
			properties.get('enable_floating_ip')
	}

	def "should render rule without probe defined"() {
		given:
			LoadBalancer loadBalancer = new LoadBalancer(resourceGroup(), 'front', null)
			LoadBalancer.Rule rule = new LoadBalancer.Rule(loadBalancer, resourceGroup(), 'front')
			rule.name('http')
		when:
			Map properties = rule.elementProperties()
		then:
			!properties.containsKey('probe_id')
	}


	def "should render nat rule"() {
		given:
			LoadBalancer loadBalancer = new LoadBalancer(resourceGroup(), 'front', null)
			LoadBalancer.NatRule natRule = new LoadBalancer.NatRule(loadBalancer, resourceGroup(), 'front')
			natRule.name('http')
			natRule.frontendPort 5001
			natRule.backendPort 5000
		when:
			Map properties = natRule.elementProperties()
		then:
			properties.get('name') == 'test-weu-lbnr-front-http'
			properties.get('loadbalancer_id') == '${azurerm_lb.test-weu-lb-front.id}'
			properties.get('frontend_ip_configuration_name') == 'test-weu-fipc-front'
			properties.get('protocol') == LoadBalancer.TransportProtocol.Tcp
			properties.get('frontend_port') == 5001
			properties.get('backend_port') == 5000
	}

	def "should render all load balancer elements"() {
		given:
			LoadBalancer loadBalancer = new LoadBalancer(resourceGroup(), 'db', null)
			loadBalancer.probe {
				name 'ssh'
				port 22
			}
			loadBalancer.probe {
				name 'www'
				port 443
			}
			loadBalancer.rule {
				name 'ssh'
				probeName 'ssh'
				frontendPort 22
				backendPort 22
			}
			loadBalancer.natRule {
				name 'www'
				frontendPort 81
				backendPort 443
			}
		when:
			String rendered = loadBalancer.renderElement()
		then:
			rendered.contains('azurerm_lb')
			rendered.contains('frontend_ip_configuration')
			rendered.contains('azurerm_lb_backend_address_pool')
		and:
			rendered.contains('azurerm_lb_probe')
			rendered.contains('test-weu-probe-db-ssh')
			rendered.contains('test-weu-probe-db-www')
		and:
			rendered.contains('azurerm_lb_rule')
			rendered.contains('test-weu-lbr-db')
		and:
			rendered.contains('azurerm_lb_nat_rule')
			rendered.contains('test-weu-lbnr-db-www')
	}
}
