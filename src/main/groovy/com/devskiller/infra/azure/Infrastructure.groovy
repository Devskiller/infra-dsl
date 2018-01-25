package com.devskiller.infra.azure

import com.devskiller.infra.azure.resource.Network
import com.devskiller.infra.azure.internal.DslContext

class Infrastructure {

	ResourceGroup resourceGroup = new ResourceGroup()

	Network network

	Components components

	Infrastructure(String name) {
		this.resourceGroup.name = name
	}

	static Infrastructure resourceGroup(String name, @DelegatesTo(Infrastructure) Closure closure) {
		return DslContext.create(new Infrastructure(name), closure)
	}

	void region(String region) {
		this.resourceGroup.region = region
	}

	void convention(Convention convention) {
		this.resourceGroup.convention = convention
	}

	void network(@DelegatesTo(Network) Closure closure) {
		network = DslContext.create(new Network(resourceGroup), closure)
	}

	void components(@DelegatesTo(Components) Closure closure) {
		components = DslContext.create(new Components(resourceGroup), closure)
	}

	String render() {
		resourceGroup.render() + '\n' + network.renderElement() + '\n' + components.renderElement()
	}

}
