package com.devskiller.infra.azure.resource

import spock.lang.Specification

import com.devskiller.infra.azure.AzureResourceGroup
import com.devskiller.infra.internal.ResourceGroup

class AzureResourceGroupAwareSpec extends Specification {

	ResourceGroup resourceGroup() {
		ResourceGroup group = new AzureResourceGroup()
		group.setName('test')
		group.setRegion('westeurope')
		group.setDomainName('devskiller.com')
		return group
	}
}
