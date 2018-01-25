import com.devskiller.infra.azure.Infrastructure

Infrastructure.resourceGroup('ci') {

	region 'westeurope'

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
		}
		component('db') {
			availabilitySet {}
		}
	}

}