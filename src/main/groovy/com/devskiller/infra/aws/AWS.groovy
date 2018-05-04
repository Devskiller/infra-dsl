package com.devskiller.infra.aws

import com.devskiller.infra.InfrastructureProvider
import com.devskiller.infra.aws.resource.Vpc
import com.devskiller.infra.internal.DslContext
import com.devskiller.infra.util.NameUtils

class AWS extends InfrastructureProvider {

	Vpc vpc

	Components components

	AWS(String name, String prefix) {
		this.resourceGroup = new AwsResourceGroup()
		this.resourceGroup.name = name
		this.resourceGroup.prefix = prefix
	}

	void region(String region) {
		this.resourceGroup.region = region
	}

	/**
	 * Defines the VPC
	 * @param closure
	 */
	void vpc(@DelegatesTo(Vpc) Closure closure) {
		vpc = DslContext.create(new Vpc(resourceGroup), closure)
	}

	/**
	 * List of the components
	 * @param closure
	 */
	void components(@DelegatesTo(Components) Closure closure) {
		components = DslContext.create(new Components(resourceGroup, vpc.dataSourceElementId()), closure)
	}


	@Override
	Provider getProvider() {
		return new Provider('aws', ['region': resourceGroup.region])
	}

	@Override
	String render() {
		NameUtils.concatenateElements('\n', [vpc?.renderElement(), components?.renderElement()])
	}

}
