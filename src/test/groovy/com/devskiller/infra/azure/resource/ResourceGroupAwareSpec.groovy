package com.devskiller.infra.azure.resource

import spock.lang.Specification

import com.devskiller.infra.azure.ResourceGroup

class ResourceGroupAwareSpec extends Specification {

	ResourceGroup resourceGroup() {
		ResourceGroup group = new ResourceGroup()
		group.setName("test")
		group.setRegion("westeurope")
		return group
	}
}
