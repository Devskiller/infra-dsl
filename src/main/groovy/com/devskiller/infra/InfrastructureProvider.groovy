package com.devskiller.infra

import javaposse.jobdsl.dsl.NoDoc

import com.devskiller.infra.internal.ResourceGroup

abstract class InfrastructureProvider {

	ResourceGroup resourceGroup

	@NoDoc
	abstract Provider getProvider()

	abstract String render()

	static class Provider {
		String name
		Map<String, Object> properties

		Provider(String name) {
			this.name = name
		}

		Provider(String name, Map<String, Object> properties) {
			this.name = name
			this.properties = properties
		}
	}
}
