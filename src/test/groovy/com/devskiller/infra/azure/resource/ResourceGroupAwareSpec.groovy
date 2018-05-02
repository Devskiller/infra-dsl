package com.devskiller.infra.azure.resource

import spock.lang.Specification

import com.devskiller.infra.internal.ResourceGroup

class ResourceGroupAwareSpec extends Specification {

	ResourceGroup resourceGroup() {
		ResourceGroup group = new ResourceGroup()
		group.setName('test')
		group.setRegion('westeurope')
		group.setDomainName('devskiller.com')
		return group
	}
}
