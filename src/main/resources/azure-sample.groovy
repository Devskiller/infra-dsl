import com.devskiller.infra.azure.Infrastructure

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
		}
		component('db') {
			availabilitySet {}
		}
	}

}