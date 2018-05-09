package com.devskiller.infra.aws.resource

import com.devskiller.infra.internal.DslContext
import com.devskiller.infra.internal.InfrastructureElement
import com.devskiller.infra.internal.InfrastructureElementCollection
import com.devskiller.infra.internal.ResourceGroup

class AmiList extends InfrastructureElementCollection {

	List<Ami> amis = new ArrayList<>()

	AmiList(ResourceGroup resourceGroup) {
		super(resourceGroup)
	}

	/**
	 * Defines AMI
	 * @param name which will be used in the VM definition
	 * @param closure
	 */
	void ami(String name, @DelegatesTo(Ami) Closure closure) {
		Ami ami = new Ami(resourceGroup, name)
		DslContext.create(ami, closure)
		amis.add(ami)
	}

	@Override
	List<? extends InfrastructureElement> getEntries() {
		return amis
	}
}
