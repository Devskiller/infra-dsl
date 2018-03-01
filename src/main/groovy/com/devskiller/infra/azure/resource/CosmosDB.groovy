package com.devskiller.infra.azure.resource

import com.devskiller.infra.azure.ResourceGroup
import com.devskiller.infra.azure.internal.InfrastructureElement

class CosmosDB extends InfrastructureElement {

	private String offerType = 'Standard'
	private String consistencyLevel
	private String failoverLocation

	CosmosDB(ResourceGroup resourceGroup, String componentName) {
		super(resourceGroup, 'azurerm_cosmosdb_account', componentName)
	}

	void consistencyLevel(String consistencyLevel) {
		this.consistencyLevel = consistencyLevel
	}

	void failoverLocation(String failoverLocation) {
		this.failoverLocation = failoverLocation
	}

	@Override
	protected Map getAsMap() {
		[
				'offer_type': offerType,
				'consistency_policy' : [
				        'consistency_level' : consistencyLevel
				],
				'failover_policy' : [
						'location' : failoverLocation,
				        'priority' : 0
				]
		]
	}
}
