package com.devskiller.infra.aws

import com.devskiller.infra.internal.DslContext
import com.devskiller.infra.internal.InfrastructureElementCollection
import com.devskiller.infra.internal.ResourceGroup

class AwsComponents extends InfrastructureElementCollection {

	List<AwsComponent> entries = []
	private final String vpcElementId

	protected AwsComponents(ResourceGroup resourceGroup, String vpcElementId) {
		super(resourceGroup)
		this.vpcElementId = vpcElementId
	}

	void component(String name, String subnetName, @DelegatesTo(AwsComponent) Closure closure) {
		entries << DslContext.create(new AwsComponent(resourceGroup, name, subnetName, vpcElementId), closure)
	}

}
