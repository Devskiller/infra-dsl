package com.devskiller.infra.azure.resource

class NetworkSpec extends ResourceGroupAwareSpec {

	def "should render default"() {
		given:
			Network network = new Network(resourceGroup())
		when:
			Map elementProperties = network.elementProperties()
		then:
			elementProperties
			elementProperties.get('name') == 'test-weu-vnet'
			elementProperties.get('address_space') == ['10.1.0.0/16']
	}

	def "should render with cidr"() {
		given:
			Network network = new Network(resourceGroup())
		when:
			network.cidr('192.168.2.0/24')
			Map elementProperties = network.elementProperties()
		then:
			elementProperties.get('address_space') == ['192.168.2.0/24']
	}

	def "should render with network id"() {
		given:
			Network network = new Network(resourceGroup())
		when:
			network.networkId(12)
			Map elementProperties = network.elementProperties()
		then:
			elementProperties.get('address_space') == ['10.12.0.0/16']
	}

}
