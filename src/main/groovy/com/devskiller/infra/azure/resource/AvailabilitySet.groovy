package com.devskiller.infra.azure.resource

import com.devskiller.infra.azure.ResourceGroup
import com.devskiller.infra.azure.internal.InfrastructureElement

class AvailabilitySet extends InfrastructureElement {

	private int updateDomains = 3
	private int faultDomains = 5
	private boolean managed = true

	AvailabilitySet(ResourceGroup resourceGroup, String componentName) {
		super(resourceGroup, 'azurerm_availability_set', componentName)
	}

	void updateDomains(int updateDomains) {
		this.updateDomains = updateDomains
	}

	void faultDomains(int faultDomains) {
		this.faultDomains = faultDomains
	}

	void managed(boolean managed) {
		this.managed = managed
	}

	@Override
	protected Map getAsMap() {
		[
				'platform_update_domain_count': updateDomains,
				'platform_fault_domain_count' : faultDomains,
				'managed'                     : managed
		]
	}
}
