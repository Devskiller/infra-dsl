package com.devskiller.infra.azure

interface Convention {

	def <RT> String getResourceQualifier(Class<RT> resourceType, String prefix, ResourceGroup resourceGroup, List<String> resourceNames)

	String getDomainName(String prefix, ResourceGroup resourceGroup, String... resourceNames)

	String regionId(ResourceGroup resourceGroup)

}
