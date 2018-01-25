package com.devskiller.infra.azure.resource

enum Protocol {
	Tcp, Udp, Both('*')

	final String label

	Protocol(String label = null) {
		this.label = label ?: name()
	}

}