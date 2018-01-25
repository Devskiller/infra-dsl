package com.devskiller.infra.azure.resource

import com.devskiller.infra.azure.ComponentElement
import com.devskiller.infra.azure.ResourceGroup

class AvailabilitySet extends ComponentElement {

	int updateDomains = 3
	int faultDomains = 5
	boolean managed = true

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
