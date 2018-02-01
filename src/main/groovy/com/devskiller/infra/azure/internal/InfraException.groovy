package com.devskiller.infra.azure.internal

class InfraException extends RuntimeException {

	InfraException(String message) {
		super(message)
	}

	@Override
	Throwable fillInStackTrace() {
		return this
	}
}
