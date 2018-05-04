package com.devskiller.infra.azure

import javaposse.jobdsl.dsl.NoDoc

import com.devskiller.infra.hcl.HclMarshaller
import com.devskiller.infra.internal.InfraException
import com.devskiller.infra.internal.ResourceGroup

class AzureResourceGroup extends ResourceGroup {

	AzureResourceGroup() {
		super(new DefaultAzureConvention())
	}

	@Override
	Map getCommonTags(String componentName, String elementName) {
		Map tags = ['env': name]
		if (componentName) {
			tags << ['component': componentName]
		}
		return ['tags': tags]
	}

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
				] + getCommonTags('',''))
	}

	@NoDoc
	Map<String, String> commonProperties(String componentName, String elementName) {
		[
				'name'               : elementName,
				'resource_group_name': getResourceQualifier(ResourceGroup.class, [])
		]
	}

	@NoDoc
	Map<String, String> location() {
		[
				'location'           : region
		]
	}
}
