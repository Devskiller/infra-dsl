package com.devskiller.infra.azure

interface Convention {

	def <RT> String getResourceQualifier(Class<RT> resourceType, ResourceGroup resourceGroup, List<String> resourceNames)

	String getDomainName(ResourceGroup resourceGroup, String... resourceNames)

}
