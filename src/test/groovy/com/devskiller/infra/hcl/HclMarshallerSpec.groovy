package com.devskiller.infra.hcl

import spock.lang.Specification

import com.devskiller.infra.InfrastructureProvider

class HclMarshallerSpec extends Specification {

	def "should marshall provider"() {
		when:
			String provider = HclMarshaller.provider(new InfrastructureProvider.Provider("azurerm"))
		then:
			provider == '\nprovider "azurerm" {\n}\n'
	}

	def "should marshall resource"() {
		when:
			String resource = HclMarshaller.resource('azurerm_virtual_network', 'vnet', [
					'name'               : 'vnet',
					'resource_group_name': 'ci',
					'security_group'     : null,
					'hidden_network'     : false,
					'subnet'             : [
							'name'          : 'subnet1',
							'address_prefix': ['10.0.1.0/24']
					]
			])
		then:
			resource == '\nresource "azurerm_virtual_network" "vnet" {\n' +
					'  name                            = "vnet"\n' +
					'  resource_group_name             = "ci"\n' +
					'  hidden_network                  = "false"\n\n' +
					'  subnet {\n' +
					'    name                          = "subnet1"\n' +
					'    address_prefix                = ["10.0.1.0/24"]\n' +
					'  }\n}\n'
	}

	def "should marshall flat list"() {
		given:
			FlatList rules = new FlatList()
			rules.add(['name': 'rule1'])
			rules.add(['name': 'rule2'])
		when:
			String resource = HclMarshaller.resource('azurerm_network_security_group', 'vpn', [
					'name'         : 'nsg-vpn',
					'security_rule': rules
			])
		then:
			resource == '\nresource "azurerm_network_security_group" "vpn" {\n' +
					'  name                            = "nsg-vpn"\n\n' +
					'  security_rule {\n' +
					'    name                          = "rule1"\n' +
					'  }\n\n' +
					'  security_rule {\n' +
					'    name                          = "rule2"\n' +
					'  }\n}\n'
	}

	def "should escape dots in resource name"() {
		given:
			FlatList rules = new FlatList()
			rules.add(['name': 'rule1'])
			rules.add(['name': 'rule2'])
		when:
			String resource = HclMarshaller.resource('azurerm_dns_zone', 'vpn.acme.com', [
					'name'     : 'vpn.acme.com'
			])
		then:
			resource == '\nresource "azurerm_dns_zone" "vpn_acme_com" {\n' +
					'  name                            = "vpn.acme.com"\n' +
					'}\n'
	}

}
