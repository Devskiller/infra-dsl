package com.devskiller.infra.aws.resource

import com.devskiller.infra.internal.InfrastructureElement
import com.devskiller.infra.internal.ResourceGroup

class VirtualMachine extends InfrastructureElement {

	private String size
	private String subnetName
	private String keyName
	private final SecurityGroup securityGroup
	private final String vpcElementId
	private String amiName

	protected VirtualMachine(ResourceGroup resourceGroup, String componentName, String elementName,
	                         SecurityGroup securityGroup, String vpcElementId) {
		super(resourceGroup, 'aws_instance', componentName)
		this.vpcElementId = vpcElementId
		this.securityGroup = securityGroup
		setElementName(elementName)
	}

	void size(String size) {
		this.size = size
	}

	void subnetName(String subnetName) {
		this.subnetName = subnetName
	}

	void keyName(String keyName) {
		this.keyName = keyName
	}

	void amiName(String amiName) {
		this.amiName = amiName
	}

	@Override
	protected Map getAsMap() {
		return [
				'instance_type'         : size,
				'vpc_security_group_ids': securityGroup.dataSourceElementId(),
				'key_name'              : keyName,
				'subnet_id'             : new Subnet(this.resourceGroup, subnetName, null, vpcElementId).dataSourceElementId(),
				'ami'                   : new Ami(this.resourceGroup, amiName).dataSourceElementId(true),
		]
	}
}
