package com.devskiller.infra.aws

import com.devskiller.infra.InfrastructureProvider
import com.devskiller.infra.aws.resource.Vpc
import com.devskiller.infra.internal.DslContext
import com.devskiller.infra.internal.ResourceGroup

class AWS extends InfrastructureProvider {

	ResourceGroup resourceGroup = new AwsResourceGroup()

	Vpc vpc

	AWS(String name, String prefix) {
		this.resourceGroup.name = name
		this.resourceGroup.prefix = prefix
	}

	void region(String region) {
		this.resourceGroup.region = region
	}

	/**
	 * Defines the Virtual Vpc
	 * @param closure
	 */
	void vpc(@DelegatesTo(Vpc) Closure closure) {
		vpc = DslContext.create(new Vpc(resourceGroup), closure)
	}

	@Override
	Provider getProvider() {
		return new Provider('aws', ['region' : resourceGroup.region])
	}

	@Override
	String render() {
		vpc.renderElement()
	}

}
