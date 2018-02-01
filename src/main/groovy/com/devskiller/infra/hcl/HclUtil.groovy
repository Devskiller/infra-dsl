package com.devskiller.infra.hcl

class HclUtil {
	static String escapeResourceName(String resourceName) {
		resourceName.replace('.', '_')
	}
}
