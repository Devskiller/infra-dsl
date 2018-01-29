import com.devskiller.infra.azure.Infrastructure
import com.devskiller.infra.azure.resource.IpAllocationMethod
import com.devskiller.infra.azure.resource.SecurityRule.RuleProtocol
import com.devskiller.infra.azure.resource.LoadBalancer.ProbeProtocol

Infrastructure.resourceGroup('ci') {

	region 'westeurope'
	domainName 'devskiller.com'

	dnsZone {}

	network {
		networkId 201
		subnets {
			subnet('ci') {
				subnetId 1
			}
			subnet('vpn') {
				subnetId 10
			}
		}
	}

	components {
		component('vpn') {
			availabilitySet {}
			publicIp {
				generateDomainName true
			}
			networkSecurityGroup {
				securityRule {
					name 'rule1'
					destinationPort 443
					protocol RuleProtocol.Tcp
				}
				securityRule {
					name 'rule2'
					destinationPort 22
					protocol RuleProtocol.Both
				}
			}
			loadBalancer {
				probe {
					name "ssh"
					port 22
					protocol ProbeProtocol.Tcp
				}
			}
			virtualMachines {
				count 2
				networkInterface {
					subnetName 'app'
				}
			}
		}
	}

}