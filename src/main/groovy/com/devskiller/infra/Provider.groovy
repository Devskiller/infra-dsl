package com.devskiller.infra

class Provider {
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
