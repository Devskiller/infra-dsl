package com.devskiller.infra.util

import java.util.regex.Matcher
import java.util.regex.Pattern

class Cidr {

	private final static Pattern CIDR_PATTERN = Pattern.compile("(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})/(\\d{1,3})")

	private final int[] octets = new int[4]
	private final int netmask

	Cidr(String cidr) {
		Matcher matcher = CIDR_PATTERN.matcher(cidr)

		if (matcher.matches()) {
			4.times {
				i -> octets[i] = Integer.parseInt(matcher.group(i + 1))
			}
			netmask = Integer.parseInt(matcher.group(5))
		} else {
			throw new IllegalArgumentException("Cannot parse CIDR: " + cidr)
		}
	}

	String getNetworkCidr() {
		return octets.join('.') + '/' + netmask
	}

	String getSubnetCidr(int subnetId) {
		int[] subnetOctets = octets.collect()
		subnetOctets[2] = subnetId
		return subnetOctets.join('.') + '/' + (netmask + 8)
	}

}
