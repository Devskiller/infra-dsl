package com.devskiller.infra.azure.resource

class VirtualMachineSpec extends ResourceGroupAwareSpec {

	def "should render default"() {
		given:
			VirtualMachine virtualMachine = new VirtualMachine(resourceGroup(), "broker", null)
			virtualMachine.setElementName('1')
		when:
			Map properties = virtualMachine.elementProperties()
		then:
			properties
			properties.name == 't-weu-broker1'
			properties.network_interface_ids == ['${azurerm_network_interface.test-weu-ni-broker-1.id}']
			properties.get('size') == 'Standard_A0'
		//storage_image_reference
	}

	def "should render customized"() {
		given:
			VirtualMachine virtualMachine = new VirtualMachine(resourceGroup(), "broker",
					new AvailabilitySet(resourceGroup(), 'broker'))
			virtualMachine.setElementName('12')
			virtualMachine.size('Standard_A2')
		when:
			Map properties = virtualMachine.elementProperties()
		then:
			properties
			properties.name == 't-weu-broker12'
			properties.availability_set_id == '${azurerm_availability_set.test-weu-as-broker.id}'
			properties.network_interface_ids == ['${azurerm_network_interface.test-weu-ni-broker-12.id}']
			properties.get('size') == 'Standard_A2'
	}
}
