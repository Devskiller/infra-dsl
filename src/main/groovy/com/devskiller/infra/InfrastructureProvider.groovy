package com.devskiller.infra

import javaposse.jobdsl.dsl.NoDoc

abstract class InfrastructureProvider {

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
