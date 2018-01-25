package com.devskiller.infra.azure.internal

class DslContext {

	static <T> T create(Class<T> dslElementClass, Closure closure) {
		return create(dslElementClass.newInstance(), closure)
	}

	static <T> T create(T dslElement, Closure closure) {
		closure.delegate = dslElement
		closure()
		return dslElement
	}

}