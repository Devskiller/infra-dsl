package com.devskiller.infra.aws

import com.devskiller.infra.aws.resource.ElasticLoadBalancer
import com.devskiller.infra.aws.resource.SecurityGroup
import com.devskiller.infra.aws.resource.VirtualMachine
import com.devskiller.infra.internal.DslContext
import com.devskiller.infra.internal.InfrastructureElement
import com.devskiller.infra.internal.InfrastructureElementCollection
import com.devskiller.infra.internal.ResourceGroup

class AwsComponent extends InfrastructureElementCollection {

	private final String vpcElementId
	private final String subnetName

	private final String name

	List<InfrastructureElement> entries = []

	AwsComponent(ResourceGroup resourceGroup, String name, String subnetName, String vpcElementId) {
		super(resourceGroup)
		this.subnetName = subnetName
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
						findDependantElement(SecurityGroup), subnetName, vpcElementId
				),
				closure)
	}

	/**
	 * Defines ELB instance.
	 *
	 * As ELB references instances it should be defined AFTER virtual machines
	 *
	 * @param closure
	 */
	void elasticLoadBalancer(@DelegatesTo(ElasticLoadBalancer) Closure closure) {
		entries << DslContext.create(new ElasticLoadBalancer(resourceGroup, name, subnetName,
				findDependantElements(VirtualMachine), findDependantElement(SecurityGroup)), closure)
	}


	private <T> T findDependantElement(Class<T> elementClass) {
		entries.find { (it.getClass() == elementClass) } as T
	}

	private <T> List<T> findDependantElements(Class<T> elementClass) {
		entries.findAll() { (it.getClass() == elementClass) } as List<T>
	}

}
