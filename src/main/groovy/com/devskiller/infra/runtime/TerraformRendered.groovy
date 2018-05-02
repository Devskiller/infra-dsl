package com.devskiller.infra.runtime

import com.devskiller.infra.InfrastructureProvider
import com.devskiller.infra.hcl.HclMarshaller

class TerraformRendered {

	private final InfrastructureProvider infrastructure

	TerraformRendered(InfrastructureProvider infrastructure) {
		this.infrastructure = infrastructure
	}

	String render() {
		HclMarshaller.provider(infrastructure.getProvider()) + '\n' + infrastructure.render()
	}

}
