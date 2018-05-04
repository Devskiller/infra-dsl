import com.devskiller.infra.Infrastructure
import com.devskiller.infra.aws.resource.SecurityRule

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

	components {
		component('vpn') {
			securityGroup {
				securityRule('openvpn-udp') {
					// must be qualified import as Idea doesn't work with two same classes name on classpath
					direction com.devskiller.infra.aws.resource.SecurityRule.Direction.Inbound
					fromPort 8080
					toPort 8081
				}
			}
		}
	}
}