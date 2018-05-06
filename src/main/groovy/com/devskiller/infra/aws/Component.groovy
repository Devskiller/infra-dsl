package com.devskiller.infra.aws

import com.devskiller.infra.aws.resource.SecurityGroup
import com.devskiller.infra.aws.resource.VirtualMachine
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

	/**
	 * Defines a single VM instance with given index
	 * @param index
	 * @param closure
	 */
	void virtualMachine(int index, @DelegatesTo(VirtualMachine) Closure closure) {
		virtualMachine(String.valueOf(index), closure)
	}

	/**
	 * Defines a single VM instance with given suffix
	 * @param suffix
	 * @param closure
	 */
	void virtualMachine(String suffix, @DelegatesTo(VirtualMachine) Closure closure) {
		entries << DslContext.create(
				new VirtualMachine(resourceGroup, name, suffix,
						findDependantElement(SecurityGroup), vpcElementId
				),
				closure)
	}


	private <T> T findDependantElement(Class<T> elementClass) {
		entries.find { (it.getClass() == elementClass) } as T
	}

}
