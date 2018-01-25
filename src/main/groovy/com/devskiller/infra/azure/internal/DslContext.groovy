package com.devskiller.infra.azure.internal

class DslContext {

	static <T> T create(T dslElement, Closure closure) {
		closure.delegate = dslElement
		closure()
		return dslElement
	}

}