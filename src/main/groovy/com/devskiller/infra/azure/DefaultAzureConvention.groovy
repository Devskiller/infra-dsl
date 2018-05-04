package com.devskiller.infra.azure

import com.devskiller.infra.azure.resource.AvailabilitySet
import com.devskiller.infra.azure.resource.CosmosDB
import com.devskiller.infra.azure.resource.DnsZone
import com.devskiller.infra.azure.resource.LoadBalancer
import com.devskiller.infra.azure.resource.NetworkPeering
import com.devskiller.infra.azure.resource.VirtualMachine
import com.devskiller.infra.azure.resource.Network
import com.devskiller.infra.azure.resource.NetworkInterface
import com.devskiller.infra.azure.resource.NetworkSecurityGroup
import com.devskiller.infra.azure.resource.PublicIp
import com.devskiller.infra.azure.resource.Subnet
import com.devskiller.infra.internal.Convention
import com.devskiller.infra.internal.ResourceGroup
import com.devskiller.infra.util.NameUtils

class DefaultAzureConvention implements Convention {

	@Override
	<RT> String getResourceQualifier(Class<RT> resourceType, String namePrefix, ResourceGroup resourceGroup, List<String> resourceNames) {
		if (resourceType == DnsZone) {
			return resourceGroup.name + '.' + resourceGroup.domainName
		} else if (resourceType == VirtualMachine) {
			return resourceGroup.name.substring(0, 1) + '-' + resourceGroup.region.substring(0, 2) + resourceGroup.region.substring(5, 6) + '-' + resourceNames.join()
		} else {
			return NameUtils.concatenateElements([namePrefix, prefix(resourceGroup), resourceId(resourceType), resourceNames])
		}
	}

	@Override
	String getDomainName(String prefix, ResourceGroup resourceGroup, String... resourceNames) {
		return NameUtils.concatenateElements([prefix, resourceGroup.name, resourceNames])
	}

	@Override
	String regionId(ResourceGroup resourceGroup) {
		resourceGroup.region.substring(0, 2) + resourceGroup.region.substring(5, 6)
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
			case VirtualMachine.Disk: return 'disk'
			case CosmosDB: return 'cdb'
			case NetworkPeering: return 'peer'
			default: throw new IllegalStateException('No convention for resource ' + resourceType)
		}
	}

	private String prefix(ResourceGroup resourceGroup) {
		return resourceGroup.name + '-' + regionId(resourceGroup)
	}

}
