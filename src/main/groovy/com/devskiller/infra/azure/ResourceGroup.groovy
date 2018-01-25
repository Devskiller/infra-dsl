package com.devskiller.infra.azure

import com.devskiller.infra.hcl.HclMarshaller

class ResourceGroup {

	String name
	String region

	Convention convention = new DefaultConvention()

	String render() {
		HclMarshaller.resource(
				'azurerm_resource_group',
				getResourceQualifier(ResourceGroup.class),
				wrapWithTags([
				'name'    : getResourceQualifier(ResourceGroup.class),
				'location': region
		]))

	}

	Map wrapWithTags(Map map) {
		map.put("tags", ["env": name])
		return map
	}

	def <RT> String getResourceQualifier(Class<RT> resourceType, String... names) {
		return convention.getResourceQualifier(resourceType, this, names)
	}

}
