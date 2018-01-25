import com.devskiller.infra.azure.Infrastructure
import com.devskiller.infra.azure.resource.IpAllocationMethod
import com.devskiller.infra.azure.resource.Protocol

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
					protocol Protocol.Tcp
				}
				securityRule {
					name 'rule2'
					destinationPort 22
					protocol Protocol.Both
				}
			}
			loadBalancer {}
		}
		component('db') {
			availabilitySet {}
			loadBalancer {
				privateIpAllocation IpAllocationMethod.Static
			}
		}
	}

}