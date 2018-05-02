package com.devskiller.infra.aws

import com.devskiller.infra.InfrastructureProvider

class AWS extends InfrastructureProvider {

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

}
