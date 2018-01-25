package com.devskiller.infra.azure.resource

class SubnetSpec extends ResourceGroupAwareSpec {

	def "should render subnet"() {
		given:
			Subnet subnet = new Subnet(resourceGroup(), 'dmz', '10.1.0.0/16')
		when:
			subnet.subnetId(12)
			Map elementProperties = subnet.elementProperties()
		then:
			elementProperties.get('name') == 'test-weu-subnet-dmz'
			elementProperties.get('address_space') == '10.1.12.0/24'
	}
}
