package com.devskiller.infra.azure.resource

import com.devskiller.infra.azure.ResourceGroup
import com.devskiller.infra.azure.internal.InfrastructureElement

class DnsZone extends InfrastructureElement {

	DnsZone(ResourceGroup resourceGroup) {
		super(resourceGroup, 'azurerm_dns_zone')
	}

	@Override
	protected Map getAsMap() {
		[:]
	}
}
