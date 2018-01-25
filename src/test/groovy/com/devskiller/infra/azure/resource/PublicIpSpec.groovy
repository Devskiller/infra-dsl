package com.devskiller.infra.azure.resource

class PublicIpSpec extends ResourceGroupAwareSpec {

	def "should render default"() {
		given:
			PublicIp publicIp = new PublicIp(resourceGroup(), 'web')
		when:
			Map processedElement = publicIp.elementProperties()
		then:
			processedElement
			processedElement.get('name') == 'test-weu-ip-web'
			processedElement.get('public_ip_address_allocation') == IpAllocationMethod.Dynamic
			processedElement.get('domain_name_label') == null
	}

	def "should render customized"() {
		given:
			PublicIp publicIp = new PublicIp(resourceGroup(), 'web')
		when:
			publicIp.allocation(IpAllocationMethod.Static)
			publicIp.domainName('ci-web')
			Map processedElement = publicIp.elementProperties()
		then:
			processedElement
			processedElement.get('name') == 'test-weu-ip-web'
			processedElement.get('public_ip_address_allocation') == IpAllocationMethod.Static
			processedElement.get('domain_name_label') == 'ci-web'
	}

	def "should render with generated domain name"() {
		given:
			PublicIp publicIp = new PublicIp(resourceGroup(), 'web')
		when:
			publicIp.allocation(IpAllocationMethod.Static)
			publicIp.generateDomainName(true)
			Map processedElement = publicIp.elementProperties()
		then:
			processedElement
			processedElement.get('name') == 'test-weu-ip-web'
			processedElement.get('public_ip_address_allocation') == IpAllocationMethod.Static
			processedElement.get('domain_name_label') == 'test-web'
	}
}
