package com.devskiller.infra.aws

import com.devskiller.infra.Infrastructure
import com.devskiller.infra.Provider
import com.devskiller.infra.azure.internal.DslContext

class AWS extends Infrastructure {

	String region

	void region(String region) {
		this.region = region
	}

	@Override
	Provider getProvider() {
		return new Provider('aws', ['region' : region])
	}

	@Override
	String render() {
		""
	}

	static AWS resourceGroup(String name, String prefix = null,
	                           @DelegatesTo(AWS) Closure closure) {
		return DslContext.create(new AWS(), closure)
	}
}
