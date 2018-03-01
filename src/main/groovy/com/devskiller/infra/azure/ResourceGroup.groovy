package com.devskiller.infra.azure

import com.devskiller.infra.azure.internal.InfraException
import com.devskiller.infra.hcl.HclMarshaller

class ResourceGroup {

	String name
	String prefix
	String region
	String domainName

	Convention convention = new DefaultConvention()

	String render() {
		if (!name) {
			throw new InfraException('Resource group name must not be empty')
		}
		if (!region) {
			throw new InfraException('Resource group region must not be empty')
		}
		HclMarshaller.resource(
				'azurerm_resource_group',
				getResourceQualifier(ResourceGroup.class),
				[
						'name'    : getResourceQualifier(ResourceGroup.class),
						'location': region
				] + getCommonTags())

	}

	Map getCommonTags(String componentName) {
		Map tags = ['env': name]
		if (componentName) {
			tags << ['component': componentName]
		}
		return ['tags': tags]
	}

	def <RT> String getResourceQualifier(Class<RT> resourceType, List<String> names = []) {
		return convention.getResourceQualifier(resourceType, prefix, this, names)
	}

	String getDomainLabel(String[] names) {
		return convention.getDomainName(prefix, this, names)
	}

	String getRegionId() {
		return convention.regionId(this)
	}

}
