package com.devskiller.infra.azure.resource

class DnsZoneSpec extends AzureResourceGroupAwareSpec {

	def "should render default"() {
		given:
			DnsZone dnsZone = new DnsZone(resourceGroup())
		when:
			Map elementProperties = dnsZone.elementProperties()
		then:
			elementProperties.get('name') == 'test.devskiller.com'
	}
}
