package com.devskiller.infra.hcl

import spock.lang.Specification

class HclMarshallerSpec extends Specification {

	def "should marshall provider"() {
		when:
			String provider = HclMarshaller.provider("azurerm")
		then:
			provider == 'provider "azurerm" {\n}\n'
	}

	def "should marshall resource"() {
		when:
			String resource = HclMarshaller.resource('azurerm_virtual_network', 'vnet', [
					'name'               : 'vnet',
					'resource_group_name': 'ci',
					'security_group'     : null,
					'subnet'             : [
							'name'          : 'subnet1',
							'address_prefix': '10.0.1.0/24'
					]
			])
		then:
			resource == 'resource "azurerm_virtual_network" "vnet" {\n' +
					'  name                           = "vnet"\n' +
					'  resource_group_name            = "ci"\n\n' +
					'  subnet {\n' +
					'    name                         = "subnet1"\n' +
					'    address_prefix               = "10.0.1.0/24"\n' +
					'  }\n\n}\n'
	}
}
