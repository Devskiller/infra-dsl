package com.devskiller.infra.azure.resource

import spock.lang.Specification

class SecurityRuleSpec extends Specification {

	def "should render default"() {
		given:
			SecurityRule rule = new SecurityRule()
			rule.name('rule1')
		when:
			Map properties = rule.getAsMap()
		then:
			properties.get('name') == 'rule1'
			properties.get('access') == SecurityRule.Access.Allow
			properties.get('protocol') == '*'
			properties.get('source_port_range') == '*'
			properties.get('destination_port_range') == '*'
			properties.get('source_address_prefix') == '*'
			properties.get('destination_address_prefix') == '*'
			properties.get('direction') == SecurityRule.Direction.Inbound
	}

	def "should render customized"() {
		given:
			SecurityRule rule = new SecurityRule()
			rule.name('rule1')
			rule.access(SecurityRule.Access.Deny)
			rule.protocol(SecurityRule.RuleProtocol.Tcp)
			rule.sourcePort(22)
			rule.sourceAddress('10.2.1.0/24')
			rule.destination(SecurityRule.Direction.Outbound)
		when:
			Map properties = rule.getAsMap()
		then:
			properties.get('name') == 'rule1'
			properties.get('access') == SecurityRule.Access.Deny
			properties.get('protocol') == 'Tcp'
			properties.get('source_port_range') == '22'
			properties.get('destination_port_range') == '*'
			properties.get('source_address_prefix') == '10.2.1.0/24'
			properties.get('destination_address_prefix') == '*'
			properties.get('direction') == SecurityRule.Direction.Outbound
	}
}
