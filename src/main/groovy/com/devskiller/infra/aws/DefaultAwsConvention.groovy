package com.devskiller.infra.aws

import com.devskiller.infra.aws.resource.SecurityGroup
import com.devskiller.infra.aws.resource.SecurityRule
import com.devskiller.infra.aws.resource.Subnet
import com.devskiller.infra.aws.resource.Vpc
import com.devskiller.infra.internal.Convention
import com.devskiller.infra.internal.ResourceGroup
import com.devskiller.infra.util.NameUtils

class DefaultAwsConvention implements Convention {

	@Override
	<RT> String getResourceQualifier(Class<RT> resourceType, String namePrefix, ResourceGroup resourceGroup, List<String> resourceNames) {
//		if (resourceType == DnsZone) {
//			return resourceGroup.name + '.' + resourceGroup.domainName
//		} else if (resourceType == VirtualMachine) {
//			return resourceGroup.name.substring(0, 1) + '-' + resourceGroup.region.substring(0, 2) + resourceGroup.region.substring(5, 6) + '-' + resourceNames.join()
//		} else {
			return NameUtils.concatenateElements([namePrefix, prefix(resourceGroup), resourceId(resourceType), resourceNames])
//		}
	}

	@Override
	String getDomainName(String prefix, ResourceGroup resourceGroup, String... resourceNames) {
		return NameUtils.concatenateElements([prefix, resourceGroup.name, resourceNames])
	}

	@Override
	String regionId(ResourceGroup resourceGroup) {
		'' + resourceGroup.region.charAt(0) + resourceGroup.region.charAt(3) + resourceGroup.region.charAt(resourceGroup.region.length()-1)
	}

	private String resourceId(Class resourceType) {
		switch (resourceType) {
			case Vpc: return 'vpc'
			case Subnet: return 'subnet'
			case SecurityGroup: return 'sg'
			case SecurityRule: return 'sr'
			default: throw new IllegalStateException('No convention for resource ' + resourceType)
		}
	}

	private String prefix(ResourceGroup resourceGroup) {
		return resourceGroup.name + '-' + regionId(resourceGroup)
	}

}
