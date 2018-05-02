package com.devskiller.infra.azure.resource

import com.devskiller.infra.internal.ResourceGroup
import com.devskiller.infra.internal.InfrastructureElement

class NetworkPeering extends InfrastructureElement {

	private final Network network
	private ResourceGroup remoteResourceGroup
	private Network remoteNetwork

	protected NetworkPeering(ResourceGroup resourceGroup, Network network) {
		super(resourceGroup, 'azurerm_virtual_network_peering')
		this.network = network
	}

	void remoteResourceGroup(String name, String prefix = null, String region = null) {
		remoteResourceGroup = new ResourceGroup()
		remoteResourceGroup.name = name
		remoteResourceGroup.prefix = prefix ?: resourceGroup.prefix
		remoteResourceGroup.region = region ?: resourceGroup.region
		remoteNetwork = new Network(remoteResourceGroup)
		setElementName(remoteResourceGroup.getRegionId() + '-' + remoteResourceGroup.name)
	}

	@Override
	Map elementProperties() {
		elementProperties(false, false)
	}

	@Override
	String renderElement() {
		return remoteNetwork.renderDataElement() + super.renderElement()
	}

	@Override
	protected Map getAsMap() {
		[
				'virtual_network_name'     : network.elementName(),
				'remote_virtual_network_id': remoteNetwork.dataSourceElementId(true)
		]
	}
}
