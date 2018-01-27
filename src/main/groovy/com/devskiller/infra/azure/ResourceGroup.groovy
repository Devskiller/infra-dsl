package com.devskiller.infra.azure

import com.devskiller.infra.hcl.HclMarshaller

class ResourceGroup {

	String name
	String region
	String domainName

	Convention convention = new DefaultConvention()

	String render() {
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
		return convention.getResourceQualifier(resourceType, this, names)
	}

	String getDomainLabel(String[] names) {
		return convention.getDomainName(this, names)
	}

}
