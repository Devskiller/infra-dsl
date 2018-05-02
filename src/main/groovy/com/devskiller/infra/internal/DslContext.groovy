package com.devskiller.infra.internal

class DslContext {

	static <T> T create(T dslElement, Closure closure) {
		closure.delegate = dslElement
		closure()
		return dslElement
	}

}