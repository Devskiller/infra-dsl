package com.devskiller.infra.aws

import com.devskiller.infra.aws.resource.SecurityGroup
import com.devskiller.infra.internal.DslContext
import com.devskiller.infra.internal.InfrastructureElement
import com.devskiller.infra.internal.InfrastructureElementCollection
import com.devskiller.infra.internal.ResourceGroup

class Component extends InfrastructureElementCollection {

	private final String vpcElementId

	String name

	List<InfrastructureElement> entries = []

	Component(ResourceGroup resourceGroup, String name, String vpcElementId) {
		super(resourceGroup)
		this.vpcElementId = vpcElementId
		this.name = name
	}

	/**
	 * Defines Network Security Group
	 * @param closure
	 */
	void securityGroup(@DelegatesTo(SecurityGroup) Closure closure) {
		entries << DslContext.create(new SecurityGroup(resourceGroup, name, vpcElementId), closure)
	}

	private <T> T findDependantElement(Class<T> elementClass) {
		entries.find { (it.getClass() == elementClass) } as T
	}

}
