package com.devskiller.infra.aws

import com.devskiller.infra.aws.resource.Ami
import com.devskiller.infra.aws.resource.SecurityGroup
import com.devskiller.infra.aws.resource.SecurityRule
import com.devskiller.infra.aws.resource.Subnet
import com.devskiller.infra.aws.resource.VirtualMachine
import com.devskiller.infra.aws.resource.Vpc
import com.devskiller.infra.internal.Convention
import com.devskiller.infra.internal.ResourceGroup
import com.devskiller.infra.util.NameUtils

class DefaultAwsConvention implements Convention {

	@Override
	<RT> String getResourceQualifier(Class<RT> resourceType, String namePrefix, ResourceGroup resourceGroup, List<String> resourceNames) {
		return NameUtils.concatenateElements([namePrefix, prefix(resourceGroup), resourceId(resourceType), resourceNames])
	}

	@Override
	String getDomainName(String prefix, ResourceGroup resourceGroup, String... resourceNames) {
		return NameUtils.concatenateElements([prefix, resourceGroup.name, resourceNames])
	}

	@Override
	String regionId(ResourceGroup resourceGroup) {
		'' + resourceGroup.region.charAt(0) + resourceGroup.region.charAt(3) + resourceGroup.region.charAt(resourceGroup.region.length() - 1)
	}

	private String resourceId(Class resourceType) {
		switch (resourceType) {
			case Vpc: return 'vpc'
			case Subnet: return 'subnet'
			case SecurityGroup: return 'sg'
			case SecurityRule: return 'sr'
			case VirtualMachine: return 'vm'
			case Ami: return 'ami'
			default: throw new IllegalStateException('No convention for resource ' + resourceType)
		}
	}

	private String prefix(ResourceGroup resourceGroup) {
		return resourceGroup.name + '-' + regionId(resourceGroup)
	}

}
