package com.devskiller.infra.runtime

import com.devskiller.infra.Infrastructure
import com.devskiller.infra.hcl.HclMarshaller

class TerraformRendered {

	private final Infrastructure infrastructure

	TerraformRendered(Infrastructure infrastructure) {
		this.infrastructure = infrastructure
	}

	String render() {
		HclMarshaller.provider(infrastructure.getProvider()) + '\n' + infrastructure.render()
	}

}
