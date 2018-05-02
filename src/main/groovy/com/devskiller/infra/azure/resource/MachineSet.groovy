package com.devskiller.infra.azure.resource

import com.devskiller.infra.internal.ResourceGroup
import com.devskiller.infra.internal.DslContext
import com.devskiller.infra.internal.InfrastructureElement

class MachineSet extends InfrastructureElement {

	private final NetworkInterface networkInterfaceTemplate
	private final VirtualMachine virtualMachineTemplate

	private final String index

	MachineSet(String index, ResourceGroup resourceGroup, String componentName,
	           NetworkSecurityGroup networkSecurityGroup, LoadBalancer loadBalancer,
	           AvailabilitySet availabilitySet) {
		super(resourceGroup, null, componentName)
		this.index = index
		networkInterfaceTemplate = new NetworkInterface(resourceGroup, componentName, networkSecurityGroup, loadBalancer)
		virtualMachineTemplate = new VirtualMachine(resourceGroup, componentName, availabilitySet)
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
		networkInterfaceTemplate.setElementName(index)
		builder.append(networkInterfaceTemplate.renderElement())
		virtualMachineTemplate.setElementName(index)
		builder.append(virtualMachineTemplate.renderElement())
		return builder.toString()
	}

	@Override
	protected Map getAsMap() {
		[:]
	}
}
