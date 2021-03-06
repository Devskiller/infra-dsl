package com.devskiller.infra.aws

import com.devskiller.infra.InfrastructureProvider
import com.devskiller.infra.aws.resource.AmiList
import com.devskiller.infra.aws.resource.Vpc
import com.devskiller.infra.internal.DslContext
import com.devskiller.infra.util.NameUtils

class AWS extends InfrastructureProvider {

	Vpc vpc

	AmiList amiList

	AwsComponents components

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
	 * Defines Machine Images that will be used during VM creation
	 * @param closure
	 */
	void amis(@DelegatesTo(AmiList) Closure closure) {
		amiList = DslContext.create(new AmiList(resourceGroup), closure)
	}

	/**
	 * List of the components
	 * @param closure
	 */
	void components(@DelegatesTo(AwsComponents) Closure closure) {
		components = DslContext.create(new AwsComponents(resourceGroup, vpc.dataSourceElementId()), closure)
	}


	@Override
	Provider getProvider() {
		return new Provider('aws', ['region': resourceGroup.region])
	}

	@Override
	String render() {
		NameUtils.concatenateElements('\n', [vpc?.renderElement(), amiList?.renderElement(), components?.renderElement()])
	}

}
