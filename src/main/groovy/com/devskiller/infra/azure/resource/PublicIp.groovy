package com.devskiller.infra.azure.resource

import com.devskiller.infra.azure.ResourceGroup
import com.devskiller.infra.azure.internal.InfrastructureElement

class PublicIp extends InfrastructureElement {

	private IpAllocationMethod allocation = IpAllocationMethod.Dynamic
	private String domainName
	private boolean generateDomainName
	private String name

	PublicIp(ResourceGroup resourceGroup, String name) {
		super(resourceGroup, 'azurerm_public_ip', name)
		this.name = name
	}

	void allocation(IpAllocationMethod allocation) {
		this.allocation = allocation
	}

	void domainName(String domainName) {
		this.domainName = domainName
	}

	void generateDomainName(boolean generateDomainName) {
		this.generateDomainName = generateDomainName
	}

	private String getDomainName() {
		return generateDomainName ? resourceGroup.getDomainLabel(name) : domainName
	}

	@Override
	protected Map getAsMap() {
		[
				'public_ip_address_allocation': allocation,
				'domain_name_label'           : getDomainName()
		]
	}
}
