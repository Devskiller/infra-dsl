package com.devskiller.infra.azure

import com.devskiller.infra.azure.resource.AvailabilitySet
import com.devskiller.infra.azure.resource.Network
import com.devskiller.infra.azure.resource.PublicIp
import com.devskiller.infra.azure.resource.Subnet

class DefaultConvention implements Convention {

	@Override
	<RT> String getResourceQualifier(Class<RT> resourceType, ResourceGroup resourceGroup, String... resourceName) {
		return prefix(resourceGroup) + "-" + String.join('-', [resourceId(resourceType), resourceName].flatten() as String[])
	}

	private String resourceId(Class resourceType) {
		switch (resourceType) {
			case ResourceGroup: return "rg"
			case Network: return "vnet"
			case Subnet: return "subnet"
			case AvailabilitySet: return "as"
			case PublicIp: return "ip"
			default: throw new IllegalStateException()
		}
	}

	private String prefix(ResourceGroup resourceGroup) {
		return resourceGroup.name + "-" + resourceGroup.region.substring(0, 2) + resourceGroup.region.substring(5, 6)
	}

}
