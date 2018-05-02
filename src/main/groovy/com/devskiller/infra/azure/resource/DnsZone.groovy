package com.devskiller.infra.azure.resource

import com.devskiller.infra.internal.ResourceGroup
import com.devskiller.infra.internal.InfrastructureElement

class DnsZone extends InfrastructureElement {

	DnsZone(ResourceGroup resourceGroup) {
		super(resourceGroup, 'azurerm_dns_zone')
	}

	@Override
	Map elementProperties() {
		elementProperties(false)
	}

	@Override
	protected Map getAsMap() {
		[:]
	}
}
