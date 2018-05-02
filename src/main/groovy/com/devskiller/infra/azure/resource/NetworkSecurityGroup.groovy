package com.devskiller.infra.azure.resource

import com.devskiller.infra.internal.ResourceGroup
import com.devskiller.infra.internal.DslContext
import com.devskiller.infra.internal.InfrastructureElement
import com.devskiller.infra.hcl.FlatList

class NetworkSecurityGroup extends InfrastructureElement {

	private final List<SecurityRule> entries = []

	NetworkSecurityGroup(ResourceGroup resourceGroup, String name) {
		super(resourceGroup, 'azurerm_network_security_group', name)
	}

	/**
	 * Defines the Security Rule
	 * @param closure
	 */
	void securityRule(@DelegatesTo(SecurityRule) Closure closure) {
		entries << DslContext.create(new SecurityRule(), closure)
	}

	@Override
	protected Map getAsMap() {
		FlatList rules = new FlatList()
		int priority = 200
		entries.each {
			rule ->
				if (!rule.hasPriority()) {
					rule.priority(priority++)
				}
				rules.add(rule.getAsMap())
		}
		return  ['security_rule': rules]
	}
}
