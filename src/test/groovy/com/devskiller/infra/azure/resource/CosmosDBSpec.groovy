package com.devskiller.infra.azure.resource

class CosmosDBSpec extends ResourceGroupAwareSpec {

	def "should render"() {
		given:
			CosmosDB cosmosDB = new CosmosDB(resourceGroup(), "data")
			cosmosDB.consistencyLevel('Strong')
			cosmosDB.failoverLocation('West Europe')
		when:
			Map processedElement = cosmosDB.elementProperties()
		then:
			processedElement
			processedElement.get('name') == 'test-weu-cdb-data'
			processedElement.get('offer_type') == 'Standard'
			processedElement.get('kind') == 'GlobalDocumentDB'
			processedElement.get('consistency_policy').get('consistency_level') == 'Strong'
			processedElement.get('failover_policy').get('location') == 'West Europe'
			processedElement.get('failover_policy').get('priority') == 0
	}

}
