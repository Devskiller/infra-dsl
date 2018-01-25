package com.devskiller.infra.azure

interface Convention {

	def <RT> String getResourceQualifier(Class<RT> resourceType, ResourceGroup resourceGroup, String... resourceNames)

}
