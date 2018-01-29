package com.devskiller.infra.azure.resource

import com.devskiller.infra.azure.ResourceGroup
import com.devskiller.infra.azure.internal.DslContext
import com.devskiller.infra.azure.internal.InfrastructureElement

class VirtualMachines extends InfrastructureElement {

	private final NetworkInterface networkInterfaceTemplate

	int count = 1
	int firstIndex = 1

	VirtualMachines(ResourceGroup resourceGroup, String componentName,
	                NetworkSecurityGroup networkSecurityGroup, LoadBalancer loadBalancer) {
		super(resourceGroup, null, componentName)
		networkInterfaceTemplate = new NetworkInterface(resourceGroup, componentName, networkSecurityGroup, loadBalancer)
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

	@Override
	String renderElement() {
		StringBuilder builder = new StringBuilder()
		count.times { index ->
			networkInterfaceTemplate.setElementName(Integer.toString(index + firstIndex))
			builder.append(networkInterfaceTemplate.renderElement())
		}
		return builder.toString()
	}

	@Override
	protected Map getAsMap() {
		[:]
	}
}
