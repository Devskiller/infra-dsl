package com.devskiller.infra.aws.resource

import com.devskiller.infra.internal.InfrastructureElement
import com.devskiller.infra.internal.ResourceGroup

class Ami extends InfrastructureElement {

	private String name
	private String owner
	private boolean mostRecent = true

	protected Ami(ResourceGroup resourceGroup, String name) {
		super(resourceGroup, 'aws_ami', name)
	}

	void mostRecent(boolean mostRecent) {
		this.mostRecent = mostRecent
	}

	void name(String name) {
		this.name = name
	}

	void owner(String owner) {
		this.owner = owner
	}

	@Override
	Map elementProperties() {
		elementProperties(false, false)
	}

	@Override
	String renderElement() {
		return super.renderDataElement(true)
	}

	@Override
	Map getAsMap() {
		[
				'most_recent': mostRecent,
				'filter' : [
				        'name' : 'name',
				        'values' : [name]
				],
				'owners' : [owner]
		]
	}
}
