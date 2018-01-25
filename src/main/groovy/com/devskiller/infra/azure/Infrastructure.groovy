package com.devskiller.infra.azure

import com.devskiller.infra.azure.internal.DslContext
import com.devskiller.infra.azure.resource.DnsZone
import com.devskiller.infra.azure.resource.Network

class Infrastructure {

	ResourceGroup resourceGroup = new ResourceGroup()

	Network network

	DnsZone dnsZone

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

	void domainName(String domainName) {
		this.resourceGroup.domainName = domainName
	}

	void convention(Convention convention) {
		this.resourceGroup.convention = convention
	}

	void network(@DelegatesTo(Network) Closure closure) {
		network = DslContext.create(new Network(resourceGroup), closure)
	}

	void dnsZone(@DelegatesTo(DnsZone) Closure closure) {
		dnsZone = DslContext.create(new DnsZone(resourceGroup), closure)
	}

	void components(@DelegatesTo(Components) Closure closure) {
		components = DslContext.create(new Components(resourceGroup), closure)
	}

	String render() {
		resourceGroup.render() + '\n' + network?.renderElement() + '\n' + dnsZone?.renderElement() + '\n' + components?.renderElement()
	}

}
