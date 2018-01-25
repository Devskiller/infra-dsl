package com.devskiller.infra.runtime

import com.devskiller.infra.azure.Infrastructure
import com.devskiller.infra.hcl.HclMarshaller

class TerraformRendered {

	private final Infrastructure infrastructure

	TerraformRendered(Infrastructure infrastructure) {
		this.infrastructure = infrastructure
	}

	String render() {
		HclMarshaller.provider('azurerm') + '\n' + infrastructure.render()
	}

}
