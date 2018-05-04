package com.devskiller.infra.internal

abstract class ResourceGroup {

	String name
	String prefix
	String region
	String domainName

	Convention convention

	ResourceGroup(Convention convention) {
		this.convention = convention
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

	abstract Map getCommonTags(String componentName, String elementName)

	abstract Map commonProperties(String componentName, String elementName)

	abstract Map location()
}
