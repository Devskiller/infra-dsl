import com.devskiller.infra.Infrastructure

Infrastructure.aws('sample') {

	region 'eu-west-1'

	vpc {
		networkId 1

		subnets {
			subnet('vpn') {
				subnetId 1
			}
		}
	}
}