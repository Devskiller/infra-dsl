package com.devskiller.infra.azure.resource

import com.devskiller.infra.azure.ResourceGroup
import com.devskiller.infra.azure.internal.InfrastructureElement

class AvailabilitySet extends InfrastructureElement {

	private int updateDomains = 5
	private int faultDomains = 3
	private boolean managed = true

	AvailabilitySet(ResourceGroup resourceGroup, String componentName) {
		super(resourceGroup, 'azurerm_availability_set', componentName)
	}

	/**
	 * Sets the number of update domains
	 * @param updateDomains
	 */
	void updateDomains(int updateDomains) {
		this.updateDomains = updateDomains
	}

	/**
	 * Sets the number of fault domains
	 * @param faultDomains
	 */
	void faultDomains(int faultDomains) {
		this.faultDomains = faultDomains
	}

	/**
	 * Should the Availability Set be configured as <b>managed</b>
	 * @param managed
	 */
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
