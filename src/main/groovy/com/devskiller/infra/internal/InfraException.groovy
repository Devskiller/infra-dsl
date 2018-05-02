package com.devskiller.infra.internal

class InfraException extends RuntimeException {

	InfraException(String message) {
		super(message)
	}

	@Override
	Throwable fillInStackTrace() {
		return this
	}
}
