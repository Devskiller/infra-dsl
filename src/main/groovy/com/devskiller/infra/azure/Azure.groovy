package com.devskiller.infra.azure

import com.devskiller.infra.InfrastructureProvider
import com.devskiller.infra.internal.Convention
import com.devskiller.infra.internal.DslContext
import com.devskiller.infra.azure.resource.DnsZone
import com.devskiller.infra.azure.resource.Network
import com.devskiller.infra.util.NameUtils

/**
 * Azure Cloud entry point
 */
class Azure extends InfrastructureProvider {

	Network network

	DnsZone dnsZone

	AzureComponents components

	Azure(String name, String prefix) {
		this.resourceGroup = new AzureResourceGroup()
		this.resourceGroup.name = name
		this.resourceGroup.prefix = prefix
	}

	/**
	 * Sets the region name (see <code>az account list-locations</code>)
	 * @param region
	 */
	void region(String region) {
		this.resourceGroup.region = region
	}

	/**
	 * Sets the dns domain name
	 * @param domainName
	 */
	void domainName(String domainName) {
		this.resourceGroup.domainName = domainName
	}

	/**
	 * Sets the custom Convention instance
	 * @param convention
	 */
	void convention(Convention convention) {
		this.resourceGroup.convention = convention
	}

	/**
	 * Defines the Virtual Network
	 * @param closure
	 */
	void network(@DelegatesTo(Network) Closure closure) {
		network = DslContext.create(new Network(resourceGroup), closure)
	}

	/**
	 * Defines the DNS Zone
	 * @param closure
	 */
	void dnsZone(@DelegatesTo(DnsZone) Closure closure) {
		dnsZone = DslContext.create(new DnsZone(resourceGroup), closure)
	}

	/**
	 * List of the components
	 * @param closure
	 */
	void components(@DelegatesTo(AzureComponents) Closure closure) {
		components = DslContext.create(new AzureComponents(resourceGroup), closure)
	}

	@Override
	String render() {
		NameUtils.concatenateElements('\n',
				[
						resourceGroup?.render(),
						network?.renderElement(),
						dnsZone?.renderElement(),
						components?.renderElement()
				].findAll({ it != null }))
	}

	@Override
	Provider getProvider() {
		return new Provider('azurerm')
	}
}
