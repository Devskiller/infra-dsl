package com.devskiller.infra.azure.resource

class MachineSetSpec extends ResourceGroupAwareSpec {

	def "should render default"() {
		given:
			MachineSet virtualMachines = new MachineSet(resourceGroup(), 'vpn', null, null, null)
		when:
			String renderedElement = virtualMachines.renderElement()
		then:
			renderedElement
			renderedElement.contains('resource "azurerm_network_interface" "test-weu-ni-vpn-1"')
			renderedElement.contains('resource "azurerm_virtual_machine" "t-weu-vpn1"')
	}

	def "should render customized"() {
		given:
			MachineSet virtualMachines = new MachineSet(resourceGroup(), 'vpn', null, null, null)
			virtualMachines.count(2)
			virtualMachines.firstIndex(15)
		when:
			String renderedElement = virtualMachines.renderElement()
		then:
			renderedElement
			renderedElement.contains('resource "azurerm_network_interface" "test-weu-ni-vpn-15"')
			renderedElement.contains('resource "azurerm_network_interface" "test-weu-ni-vpn-16"')
			renderedElement.contains('resource "azurerm_virtual_machine" "t-weu-vpn15"')
			renderedElement.contains('resource "azurerm_virtual_machine" "t-weu-vpn16"')
	}
}
