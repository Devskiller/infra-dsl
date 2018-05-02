package com.devskiller.infra.azure.resource

import groovy.transform.PackageScope

import com.devskiller.infra.internal.ResourceGroup
import com.devskiller.infra.internal.DslContext
import com.devskiller.infra.internal.InfrastructureElement

class LoadBalancer extends InfrastructureElement {

	private final String name
	private final BackendPool backendPool

	private final List<Probe> probes = []
	private final List<Rule> rules = []
	private final List<NatRule> natRules = []

	private FrontendIpConfiguration frontendIpConfiguration = new FrontendIpConfiguration()

	LoadBalancer(ResourceGroup resourceGroup, String name, PublicIp publicIp) {
		super(resourceGroup, 'azurerm_lb', name)
		this.backendPool = new BackendPool(resourceGroup, name)
		this.name = name
		this.frontendIpConfiguration.publicIp = publicIp
	}

	void privateIpAllocation(IpAllocationMethod privateIpAllocation) {
		this.frontendIpConfiguration.privateIpAllocation = privateIpAllocation
	}

	void probe(@DelegatesTo(Probe) Closure closure) {
		probes << DslContext.create(new Probe(resourceGroup, name), closure)
	}

	void rule(@DelegatesTo(Rule) Closure closure) {
		rules << DslContext.create(new Rule(resourceGroup, name), closure)
	}

	void natRule(@DelegatesTo(NatRule) Closure closure) {
		natRules << DslContext.create(new NatRule(resourceGroup, name), closure)
	}

	@PackageScope
	BackendPool getBackendPool() {
		return backendPool
	}

	@PackageScope
	List<NatRule> getNatRules() {
		return natRules
	}

	@Override
	protected Map getAsMap() {
		return frontendIpConfiguration.getAsMap()
	}

	@Override
	String renderElement() {
		return super.renderElement() + backendPool.renderElement() \
                    + probes.collect { it.renderElement() }.join() \
                    + rules.collect { it.renderElement() }.join() \
                    + natRules.collect { it.renderElement() }.join()
	}

	class FrontendIpConfiguration {

		private PublicIp publicIp
		private IpAllocationMethod privateIpAllocation = IpAllocationMethod.Dynamic

		protected Map getAsMap() {
			Map frontConfig = [
					'name': name()
			]

			if (publicIp) {
				frontConfig << ['public_ip_address_id': publicIp.dataSourceElementId()]
			} else {
				frontConfig << ['private_ip_address_allocation': privateIpAllocation.name()]
			}

			return [
					'frontend_ip_configuration': frontConfig
			]
		}

		private String name() {
			LoadBalancer.this.resourceGroup.getResourceQualifier(this.class, [name])
		}
	}

	class BackendPool extends InfrastructureElement {

		protected BackendPool(ResourceGroup resourceGroup, String name) {
			super(resourceGroup, 'azurerm_lb_backend_address_pool', name)
		}

		@Override
		Map elementProperties() {
			return super.elementProperties(false, false)
		}

		@Override
		protected Map getAsMap() {
			[
					'loadbalancer_id': LoadBalancer.this.dataSourceElementId()
			]
		}
	}

	class Probe extends InfrastructureElement {

		private int port
		private ProbeProtocol protocol = ProbeProtocol.Tcp
		private String requestPath
		private Integer intervalInSeconds
		private Integer numberOfProbes

		protected Probe(ResourceGroup resourceGroup, String name) {
			super(resourceGroup, 'azurerm_lb_probe', name)
		}

		void name(String name) {
			setElementName(name)
		}

		void port(int port) {
			this.port = port
		}

		void protocol(ProbeProtocol protocol) {
			this.protocol = protocol
		}

		void requestPath(String requestPath) {
			this.requestPath = requestPath
		}

		void intervalInSeconds(Integer intervalInSeconds) {
			this.intervalInSeconds = intervalInSeconds
		}

		void numberOfProbes(Integer numberOfProbes) {
			this.numberOfProbes = numberOfProbes
		}

		@Override
		Map elementProperties() {
			return super.elementProperties(false, false)
		}

		@Override
		protected Map getAsMap() {
			[
					'loadbalancer_id'    : LoadBalancer.this.dataSourceElementId(),
					'port'               : port,
					'protocol'           : protocol,
					'request_path'       : requestPath,
					'interval_in_seconds': intervalInSeconds,
					'number_of_probes'   : numberOfProbes
			]
		}
	}

	abstract class AbstractRule extends InfrastructureElement {

		private TransportProtocol protocol = TransportProtocol.Tcp
		private int backendPort
		private Boolean enableFloatingIp

		protected AbstractRule(ResourceGroup resourceGroup, String resourceType, String componentName) {
			super(resourceGroup, resourceType, componentName)
		}

		void name(String name) {
			setElementName(name)
		}

		void protocol(TransportProtocol protocol) {
			this.protocol = protocol
		}

		void backendPort(int backendPort) {
			this.backendPort = backendPort
		}

		void enableFloatingIp(Boolean enableFloatingIp) {
			this.enableFloatingIp = enableFloatingIp
		}

		@Override
		Map elementProperties() {
			return super.elementProperties(false, false)
		}

		@Override
		protected Map getAsMap() {
			return [
					'loadbalancer_id'               : LoadBalancer.this.dataSourceElementId(),
					'frontend_ip_configuration_name': LoadBalancer.this.frontendIpConfiguration.name(),
					'protocol'                      : protocol,
					'backend_port'                  : backendPort,
					'enable_floating_ip'            : enableFloatingIp
			]
		}
	}

	class Rule extends AbstractRule {

		private int frontendPort
		private LoadDistribution loadDistribution
		private String probeName

		protected Rule(ResourceGroup resourceGroup, String componentName) {
			super(resourceGroup, 'azurerm_lb_rule', componentName)
		}

		void frontendPort(int frontendPort) {
			this.frontendPort = frontendPort
		}

		void loadDistribution(LoadDistribution loadDistribution) {
			this.loadDistribution = loadDistribution
		}

		void probeName(String probeName) {
			this.probeName = probeName
		}

		@Override
		protected Map getAsMap() {
			Map map = super.getAsMap() + [
					'frontend_port'    : frontendPort,
					'load_distribution': loadDistribution,
					'backend_address_pool_id' : LoadBalancer.this.getBackendPool().dataSourceElementId()
			]
			if (probeName) {
				map << ['probe_id': LoadBalancer.this.probes.find { it.elementName == probeName }.dataSourceElementId()]
			}
			return map
		}

		}

	class NatRule extends AbstractRule {

		private int frontendPort

		protected NatRule(ResourceGroup resourceGroup, String componentName) {
			super(resourceGroup, 'azurerm_lb_nat_rule', componentName)
		}

		void frontendPort(int frontendPort) {
			this.frontendPort = frontendPort
		}

		@Override
		protected Map getAsMap() {
			return super.getAsMap() + [
					'frontend_port': frontendPort
			]
		}
	}

	enum ProbeProtocol {

		Http, Tcp
	}

	enum TransportProtocol {

		Tcp, Udp
	}

	enum LoadDistribution {

		Default, SourceIP, SourceIPProtocol
	}
}
