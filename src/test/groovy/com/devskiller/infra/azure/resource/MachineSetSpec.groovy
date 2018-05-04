package com.devskiller.infra.azure.resource

class MachineSetSpec extends AzureResourceGroupAwareSpec {

	def "should render default"() {
		given:
			MachineSet virtualMachines = new MachineSet('1', resourceGroup(), 'vpn', null, null, null)
		when:
			String renderedElement = virtualMachines.renderElement()
		then:
			renderedElement
			renderedElement.contains('resource "azurerm_network_interface" "test-weu-ni-vpn-1"')
			renderedElement.contains('resource "azurerm_virtual_machine" "t-weu-vpn1"')
	}
}
