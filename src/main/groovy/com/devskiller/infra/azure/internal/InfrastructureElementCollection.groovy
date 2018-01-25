package com.devskiller.infra.azure.internal

import com.devskiller.infra.azure.ResourceGroup

abstract class InfrastructureElementCollection extends InfrastructureElement {

	protected InfrastructureElementCollection(ResourceGroup resourceGroup, String name = null) {
		super(resourceGroup, name)
	}

	abstract List<? extends InfrastructureElement> getEntries()

	@Override
	protected Map getAsMap() {
		throw new UnsupportedOperationException()
	}

	@Override
	String renderElement() {
		StringBuilder builder = new StringBuilder()
		getEntries().each {
			element ->
				builder.append(element.renderElement())
		}
		return builder
	}
}
