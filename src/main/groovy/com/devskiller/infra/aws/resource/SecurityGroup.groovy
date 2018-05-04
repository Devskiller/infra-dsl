package com.devskiller.infra.aws.resource

import com.devskiller.infra.internal.DslContext
import com.devskiller.infra.internal.InfrastructureElement
import com.devskiller.infra.internal.ResourceGroup
import com.devskiller.infra.util.NameUtils

class SecurityGroup extends InfrastructureElement {

	private final List<SecurityRule> entries = []

	private final String vpcElementId

	SecurityGroup(ResourceGroup resourceGroup, String name, String vpcElementId) {
		super(resourceGroup, 'aws_security_group', name)
		this.vpcElementId = vpcElementId
	}

	/**
	 * Defines the Security Rule
	 * @param closure
	 */
	void securityRule(String ruleName, @DelegatesTo(SecurityRule) Closure closure) {
		SecurityRule rule = new SecurityRule(resourceGroup, componentName, dataSourceElementId())
		rule.setElementName(ruleName)
		entries << DslContext.create(rule, closure)
	}

	@Override
	protected Map getAsMap() {
		return [
				'name'  : elementName(),
				'vpc_id': vpcElementId
		]
	}

	@Override
	String renderElement() {
		List<Object> objects = [super.renderElement(), entries.collect { it.renderElement() }]
		return NameUtils.concatenateElements('\n', objects)
	}
}
