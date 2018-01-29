package com.devskiller.infra.azure.resource

import com.devskiller.infra.azure.ResourceGroup
import com.devskiller.infra.azure.internal.DslContext
import com.devskiller.infra.azure.internal.InfrastructureElement

class MachineSet extends InfrastructureElement {

	private final NetworkInterface networkInterfaceTemplate
	private final VirtualMachine virtualMachineTemplate

	int count = 1
	int firstIndex = 1

	MachineSet(ResourceGroup resourceGroup, String componentName,
	           NetworkSecurityGroup networkSecurityGroup, LoadBalancer loadBalancer,
	           AvailabilitySet availabilitySet) {
		super(resourceGroup, null, componentName)
		networkInterfaceTemplate = new NetworkInterface(resourceGroup, componentName, networkSecurityGroup, loadBalancer)
		virtualMachineTemplate = new VirtualMachine(resourceGroup, componentName, availabilitySet)
	}

	void count(int count) {
		this.count = count
	}

	void firstIndex(int firstIndex) {
		this.firstIndex = firstIndex
	}

	void networkInterface(@DelegatesTo(NetworkInterface) Closure closure) {
		DslContext.create(networkInterfaceTemplate, closure)
	}

	void instance(@DelegatesTo(VirtualMachine) Closure closure) {
		DslContext.create(virtualMachineTemplate, closure)
	}

	@Override
	String renderElement() {
		StringBuilder builder = new StringBuilder()
		count.times { index ->
			networkInterfaceTemplate.setElementName(Integer.toString(index + firstIndex))
			builder.append(networkInterfaceTemplate.renderElement())
			virtualMachineTemplate.setElementName(Integer.toString(index + firstIndex))
			builder.append(virtualMachineTemplate.renderElement())
		}
		return builder.toString()
	}

	@Override
	protected Map getAsMap() {
		[:]
	}
}
