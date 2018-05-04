package com.devskiller.infra.azure.resource

class NetworkPeeringSpec extends AzureResourceGroupAwareSpec {

	def "should render"() {
		given:
			Network network = new Network(resourceGroup())
			NetworkPeering networkPeering = new NetworkPeering(resourceGroup(), network)
			networkPeering.remoteResourceGroup('dev')
		when:
			Map processedElement = networkPeering.elementProperties()
		then:
			processedElement
			processedElement.get('name') == 'test-weu-peer-weu-dev'
			processedElement.get('virtual_network_name') == 'test-weu-vnet'
			processedElement.get('remote_virtual_network_id') == '${data.azurerm_virtual_network.dev-weu-vnet.id}'
		and:
			networkPeering.renderElement().contains('data "azurerm_virtual_network" "dev-weu-vnet"')
	}

}
