package com.devskiller.infra.aws.resource

import com.devskiller.infra.internal.InfrastructureElement
import com.devskiller.infra.internal.ResourceGroup

class SecurityRule extends InfrastructureElement {

	private String securityGroupElementId
	private Direction direction = Direction.Inbound
	private RuleProtocol protocol = RuleProtocol.all
	private int fromPort
	private int toPort
	private String description
	private Boolean self

	SecurityRule(ResourceGroup resourceGroup, String name, String securityGroupElementId) {
		super(resourceGroup, 'aws_security_group_rule', name)
		this.securityGroupElementId = securityGroupElementId
	}

	void direction(Direction direction) {
		this.direction = direction
	}

	void protocol(RuleProtocol protocol) {
		this.protocol = protocol
	}

	void fromPort(int fromPort) {
		this.fromPort = fromPort
	}

	void toPort(int toPort) {
		this.toPort = toPort
	}

	void description(String description) {
		this.description = description
	}

	void self(Boolean self) {
		this.self = self
	}

	@Override
	Map getAsMap() {
		Map map = [
				'security_group_id': securityGroupElementId,
				'type'             : direction?.label,
				'protocol'         : protocol,
				'from_port'        : fromPort,
				'to_port'          : toPort,
				'description'      : description
		]
		if (self != null) {
			map << ['self': self]
		}
		return map
	}

	static enum RuleProtocol {

		icmp, tcp, udp, all

	}

	static enum Direction {

		Inbound('ingress'), Outbound('egress')

		final String label

		Direction(String label) {
			this.label = label
		}
	}
}
