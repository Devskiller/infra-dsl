package com.devskiller.infra.azure

import com.devskiller.infra.azure.resource.AvailabilitySet
import com.devskiller.infra.azure.resource.DnsZone
import com.devskiller.infra.azure.resource.LoadBalancer
import com.devskiller.infra.azure.resource.Network
import com.devskiller.infra.azure.resource.NetworkInterface
import com.devskiller.infra.azure.resource.NetworkSecurityGroup
import com.devskiller.infra.azure.resource.PublicIp
import com.devskiller.infra.azure.resource.Subnet

class DefaultConvention implements Convention {

	@Override
	<RT> String getResourceQualifier(Class<RT> resourceType, ResourceGroup resourceGroup, List<String> resourceNames) {
		if (resourceType == DnsZone) {
			return resourceGroup.name + '.' + resourceGroup.domainName
		}
		return concatenateElements([prefix(resourceGroup), resourceId(resourceType), resourceNames])
	}

	@Override
	String getDomainName(ResourceGroup resourceGroup, String... resourceNames) {
		return concatenateElements([resourceGroup.name, resourceNames])
	}

	private String resourceId(Class resourceType) {
		switch (resourceType) {
			case ResourceGroup: return 'rg'
			case Network: return 'vnet'
			case Subnet: return 'subnet'
			case AvailabilitySet: return 'as'
			case PublicIp: return 'ip'
			case NetworkSecurityGroup: return 'nsg'
			case LoadBalancer: return 'lb'
			case LoadBalancer.FrontendIpConfiguration: return 'fipc'
			case LoadBalancer.BackendPool: return 'bap'
			case LoadBalancer.Probe: return 'probe'
			case LoadBalancer.Rule: return 'lbr'
			case LoadBalancer.NatRule: return 'lbnr'
			case NetworkInterface: return 'ni'
			case NetworkInterface.IpConfiguration: return 'nipc'
			default: throw new IllegalStateException()
		}
	}

	private String prefix(ResourceGroup resourceGroup) {
		return resourceGroup.name + '-' + resourceGroup.region.substring(0, 2) + resourceGroup.region.substring(5, 6)
	}

	private String concatenateElements(List<Serializable> nameElements) {
		String.join('-', nameElements.flatten() as String[])
	}

}
