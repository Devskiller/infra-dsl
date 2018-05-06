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

	amis {
		ami('ubuntu') {
			name 'ubuntu/images/hvm-ssd/ubuntu-trusty-14.04-amd64-server-*'
			owner '099720109477'
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
			virtualMachine(1) {
				size 't2.micro'
				subnetName 'vpn'
				keyName 'private-key.pem'
				amiName 'ubuntu'
			}
		}
	}
}