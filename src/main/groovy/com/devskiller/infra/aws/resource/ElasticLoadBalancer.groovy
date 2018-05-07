package com.devskiller.infra.aws.resource

import com.devskiller.infra.hcl.FlatList
import com.devskiller.infra.internal.DslContext
import com.devskiller.infra.internal.InfrastructureElement
import com.devskiller.infra.internal.ResourceGroup

class ElasticLoadBalancer extends InfrastructureElement {

	private final String subnetName
	private final List<VirtualMachine> machines
	private final SecurityGroup securityGroup

	private final List<Listener> listeners = new ArrayList()
	private final List<HealthCheck> healthChecks = new ArrayList()

	ElasticLoadBalancer(ResourceGroup resourceGroup, String componentName, String subnetName,
	                    List<VirtualMachine> machines, SecurityGroup securityGroup) {
		super(resourceGroup, 'aws_elb', componentName)
		this.securityGroup = securityGroup
		this.subnetName = subnetName
		this.machines = machines
	}

	@Override
	protected Map getAsMap() {
		Map properties = [
				'name'           : elementName(),
				'subnets'        : new Subnet(this.resourceGroup, subnetName, null, null).dataSourceElementId(),
				'security_groups': [securityGroup.dataSourceElementId()],
				'instances'      : machines.collect { it.dataSourceElementId() },
		]

		FlatList listenersMap = new FlatList()
		listeners.each {
			listenersMap.add(it.asMap)
		}
		properties << ['listener': listenersMap]

		FlatList healthChecksMap = new FlatList()
		healthChecks.each {
			healthChecksMap.add(it.asMap)
		}
		properties << ['health_check': healthChecksMap]

		return properties
	}

	void listener(@DelegatesTo(Listener) Closure closure) {
		listeners << DslContext.create(new Listener(), closure)
	}

	void healthCheck(@DelegatesTo(HealthCheck) Closure closure) {
		healthChecks << DslContext.create(new HealthCheck(), closure)
	}

	static class Listener {

		int instancePort
		ListenerProtocol instanceProtocol
		int lbPort
		ListenerProtocol lbProtocol
		String sslCertificateId

		void instancePort(int instancePort) {
			this.instancePort = instancePort
		}

		void instanceProtocol(ListenerProtocol instanceProtocol) {
			this.instanceProtocol = instanceProtocol
		}

		void lbPort(int lbPort) {
			this.lbPort = lbPort
		}

		void lbProtocol(ListenerProtocol lbProtocol) {
			this.lbProtocol = lbProtocol
		}

		void sslCertificateId(String sslCertificateId) {
			this.sslCertificateId = sslCertificateId
		}

		Map getAsMap() {
			[
					'instance_port'     : instancePort,
					'instance_protocol' : instanceProtocol,
					'lb_port'           : lbPort,
					'lb_protocol'       : lbProtocol,
					'ssl_certificate_id': sslCertificateId
			]
		}
	}

	static class HealthCheck {

		int healthyThreshold
		int unhealthyThreshold
		String target
		int interval
		int timeout

		void healthyThreshold(int healthyThreshold) {
			this.healthyThreshold = healthyThreshold
		}

		void unhealthyThreshold(int unhealthyThreshold) {
			this.unhealthyThreshold = unhealthyThreshold
		}

		void target(String target) {
			this.target = target
		}

		void interval(int interval) {
			this.interval = interval
		}

		void timeout(int timeout) {
			this.timeout = timeout
		}

		Map getAsMap() {
			[
					'healthy_threshold'  : healthyThreshold,
					'unhealthy_threshold': unhealthyThreshold,
					'target'             : target,
					'interval'           : interval,
					'timeout'            : timeout
			]
		}
	}
}
