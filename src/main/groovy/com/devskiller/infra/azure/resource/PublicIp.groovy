package com.devskiller.infra.azure.resource

import com.devskiller.infra.azure.ResourceGroup
import com.devskiller.infra.azure.internal.InfrastructureElement

class PublicIp extends InfrastructureElement {

	private IpAllocationMethod allocation = IpAllocationMethod.Dynamic
	private String domainName

	PublicIp(ResourceGroup resourceGroup, String name) {
		super(resourceGroup, 'azurerm_public_ip', name)
	}

	void allocation(IpAllocationMethod allocation) {
		this.allocation = allocation
	}

	void domainName(String domainName) {
		this.domainName = domainName
	}

	@Override
	protected Map getAsMap() {
		[
				'public_ip_address_allocation': allocation,
				'domain_name_label'           : domainName
		]
	}
}
