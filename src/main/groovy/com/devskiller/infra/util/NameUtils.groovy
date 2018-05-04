package com.devskiller.infra.util

class NameUtils {

	static String concatenateElements(List nameElements) {
		return concatenateElements('-', nameElements)
	}

	static String concatenateElements(String delimiter, List nameElements) {
		String.join(delimiter, nameElements.flatten().findAll({it != null}) as String[])
	}

}
