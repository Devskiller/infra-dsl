package com.devskiller.infra.azure.resource

class SecurityRule {

	private String name
	private Access access = Access.Allow
	private RuleProtocol protocol = RuleProtocol.Any
	private Direction direction = Direction.Inbound
	private String sourcePort = '*'
	private String destinationPort = '*'
	private String sourceAddress = '*'
	private String destinationAddress = '*'
	private int priority

	void name(String name) {
		this.name = name
	}

	void access(Access access) {
		this.access = access
	}

	void protocol(RuleProtocol protocol) {
		this.protocol = protocol
	}

	void destination(Direction destination) {
		this.direction = destination
	}

	void sourcePort(String sourcePort) {
		this.sourcePort = sourcePort
	}

	void sourcePort(int sourcePort) {
		this.sourcePort = sourcePort
	}

	void destinationPort(String destinationPort) {
		this.destinationPort = destinationPort
	}

	void destinationPort(int destinationPort) {
		this.destinationPort = destinationPort
	}

	void sourceAddress(String sourceAddress) {
		this.sourceAddress = sourceAddress
	}

	void destinationAddress(String destinationAddress) {
		this.destinationAddress = destinationAddress
	}

	void priority(int priority) {
		this.priority = priority
	}

	boolean hasPriority() {
		return priority > 0
	}

	Map getAsMap() {
		[
				'name'                      : name,
				'access'                    : access,
				'protocol'                  : protocol?.label,
				'source_port_range'         : sourcePort,
				'destination_port_range'    : destinationPort,
				'source_address_prefix'     : sourceAddress,
				'destination_address_prefix': destinationAddress,
				'priority'                  : priority,
				'direction'                 : direction
		]
	}

	static enum Access {

		Allow, Deny
	}

	static enum RuleProtocol {

		Tcp, Udp, Any('*')

		final String label

		RuleProtocol(String label = null) {
			this.label = label ?: name()
		}
	}

	static enum Direction {

		Inbound, Outbound
	}
}
