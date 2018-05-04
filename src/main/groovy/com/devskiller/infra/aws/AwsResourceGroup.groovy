package com.devskiller.infra.aws

import com.devskiller.infra.internal.ResourceGroup

class AwsResourceGroup extends ResourceGroup {

	AwsResourceGroup() {
		super(new DefaultAwsConvention())
	}

	@Override
	Map getCommonTags(String componentName, String elementName) {
		return ['tags':
				        [
						        'Name': elementName,
						        'Env' : name
				        ]
		]
	}

	@Override
	Map commonProperties(String componentName, String elementName) {
		return [:]
	}

	@Override
	Map location() {
		return [:]
	}
}
