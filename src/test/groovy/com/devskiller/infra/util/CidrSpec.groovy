package com.devskiller.infra.util

import spock.lang.Specification

class CidrSpec extends Specification {

	def "should parse and return cidr"() {
		given:
			String cidrString = "10.2.0.0/16"
		when:
			Cidr cidr = new Cidr(cidrString)
		then:
			cidr.getNetworkCidr() == cidrString
			cidr.getSubnetCidr(10) == "10.2.10.0/24"
	}
}
