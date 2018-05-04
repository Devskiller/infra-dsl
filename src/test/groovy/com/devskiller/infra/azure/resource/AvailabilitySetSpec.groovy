package com.devskiller.infra.azure.resource

class AvailabilitySetSpec extends AzureResourceGroupAwareSpec {

	def "should render default"() {
		given:
			AvailabilitySet availabilitySet = new AvailabilitySet(resourceGroup(), "db")
		when:
			Map processedElement = availabilitySet.elementProperties()
		then:
			processedElement
			processedElement.get('name') == 'test-weu-as-db'
			processedElement.get('platform_update_domain_count') == 5
			processedElement.get('platform_fault_domain_count') == 3
			processedElement.get('managed') == true
	}

	def "should render custom"() {
		given:
			AvailabilitySet availabilitySet = new AvailabilitySet(resourceGroup(), "db")
		when:
			availabilitySet.managed(false)
			availabilitySet.updateDomains(2)
			availabilitySet.faultDomains(4)
			Map processedElement = availabilitySet.elementProperties()
		then:
			processedElement
			processedElement.get('name') == 'test-weu-as-db'
			processedElement.get('platform_update_domain_count') == 2
			processedElement.get('platform_fault_domain_count') == 4
			processedElement.get('managed') == false
	}

}
