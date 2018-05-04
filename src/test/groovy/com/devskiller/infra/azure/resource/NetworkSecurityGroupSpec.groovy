package com.devskiller.infra.azure.resource

import com.devskiller.infra.hcl.FlatList

class NetworkSecurityGroupSpec extends AzureResourceGroupAwareSpec {

	def "should render default"() {
		given:
			NetworkSecurityGroup nsg = new NetworkSecurityGroup(resourceGroup(), 'web')
			nsg.securityRule {
				name 'rule1'
			}
			nsg.securityRule {
				name 'rule2'
			}
		when:
			Map properties = nsg.getAsMap()
		then:
			FlatList rules = properties.get('security_rule')
			rules.size() == 2
		and:
			Map rule1 = rules.find { it.name == 'rule1' }
			rule1.get('priority') == 200
		and:
			Map rule2 = rules.find { it.name == 'rule2' }
			rule2.get('priority') == 201
	}
}
