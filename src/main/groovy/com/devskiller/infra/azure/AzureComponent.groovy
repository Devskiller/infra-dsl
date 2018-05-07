package com.devskiller.infra.azure

import com.devskiller.infra.internal.DslContext
import com.devskiller.infra.internal.InfrastructureElement
import com.devskiller.infra.internal.InfrastructureElementCollection
import com.devskiller.infra.azure.resource.AvailabilitySet
import com.devskiller.infra.azure.resource.CosmosDB
import com.devskiller.infra.azure.resource.LoadBalancer
import com.devskiller.infra.azure.resource.MachineSet
import com.devskiller.infra.azure.resource.NetworkSecurityGroup
import com.devskiller.infra.azure.resource.PublicIp
import com.devskiller.infra.internal.ResourceGroup

class AzureComponent extends InfrastructureElementCollection {

	String name
	List<InfrastructureElement> entries = []

	AzureComponent(ResourceGroup resourceGroup, String name) {
		super(resourceGroup)
		this.name = name
	}

	/**
	 * Defines Availability Set
	 * @param closure
	 */
	void availabilitySet(@DelegatesTo(AvailabilitySet) Closure closure) {
		entries << DslContext.create(new AvailabilitySet(resourceGroup, name), closure)
	}

	/**
	 * Defines Public IP Address
	 * @param closure
	 */
	void publicIp(@DelegatesTo(PublicIp) Closure closure) {
		entries << DslContext.create(new PublicIp(resourceGroup, name), closure)
	}

	/**
	 * Defines Network Security Group
	 * @param closure
	 */
	void networkSecurityGroup(@DelegatesTo(NetworkSecurityGroup) Closure closure) {
		entries << DslContext.create(new NetworkSecurityGroup(resourceGroup, name), closure)
	}

	/**
	 * Defines Load Balancer
	 * @param closure
	 */
	void loadBalancer(@DelegatesTo(LoadBalancer) Closure closure) {
		entries << DslContext.create(
				new LoadBalancer(resourceGroup, name,
						findDependantElement(PublicIp)),
				closure)
	}

	/**
	 * Defines CosmosDB
	 * @param closure
	 */
	void cosmosDB(@DelegatesTo(CosmosDB) Closure closure) {
		entries << DslContext.create(new CosmosDB(resourceGroup, name), closure)
	}

	/**
	 * Defines a single VM instance with given index
	 * @param index
	 * @param closure
	 */
	void virtualMachine(int index, @DelegatesTo(MachineSet) Closure closure) {
		virtualMachine(String.valueOf(index), closure)
	}

	/**
	 * Defines a single VM instance with given suffix
	 * @param suffix
	 * @param closure
	 */
	void virtualMachine(String suffix, @DelegatesTo(MachineSet) Closure closure) {
		entries << DslContext.create(
				new MachineSet(suffix, resourceGroup, name,
						findDependantElement(NetworkSecurityGroup),
						findDependantElement(LoadBalancer),
						findDependantElement(AvailabilitySet)
				),
				closure)
	}

	private <T> T findDependantElement(Class<T> elementClass) {
		entries.find { (it.getClass() == elementClass) } as T
	}

}
