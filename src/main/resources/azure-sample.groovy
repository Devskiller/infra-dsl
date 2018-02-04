import com.devskiller.infra.azure.Infrastructure
import com.devskiller.infra.azure.resource.LoadBalancer.ProbeProtocol
import com.devskiller.infra.azure.resource.NetworkSecurityGroup
import com.devskiller.infra.azure.resource.SecurityRule.RuleProtocol

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
				defaultSecurityRules(delegate)
				securityRule {
					name 'metrics'
					destinationPort 9998
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
					subnetName 'vpn'
					enableAcceleratedNetworking true
				}
				instance {
					size 'Standard_A0'
					image {
						publisher 'OpenLogic'
						offer 'CentOS'
						sku '7.3'
					}
					osProfile {
						adminUsername 'root'
						adminPassword 'password'
					}
				}
			}
		}
	}

}

void defaultSecurityRules(NetworkSecurityGroup networkSecurityGroup) {
	networkSecurityGroup {
		securityRule {
			name 'rule1'
			destinationPort 443
			protocol RuleProtocol.Tcp

		}
		securityRule {
			name 'rule2'
			destinationPort 22
			protocol RuleProtocol.Any
		}
	}

}