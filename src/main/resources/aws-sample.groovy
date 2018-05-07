import com.devskiller.infra.Infrastructure
import com.devskiller.infra.aws.resource.ListenerProtocol

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
		component('vpn', 'vpn') {
			securityGroup {
				securityRule('openvpn-udp') {
					// must be qualified import as Idea doesn't work with two same classes name on classpath
					direction com.devskiller.infra.aws.resource.SecurityRule.Direction.Inbound
					fromPort 8080
					toPort 8081
				}
			}
			for (i in 1..2) {
				virtualMachine(i) {
					size 't2.micro'
					keyName 'private-key.pem'
					amiName 'ubuntu'
				}
			}
			elasticLoadBalancer {
				listener {
					instancePort 8080
					instanceProtocol ListenerProtocol.HTTP
					lbPort 80
					lbProtocol ListenerProtocol.HTTP
				}

				listener {
					instancePort 22
					instanceProtocol ListenerProtocol.TCP
					lbPort 22
					lbProtocol ListenerProtocol.TCP
				}

				healthCheck {
					healthyThreshold 2
					unhealthyThreshold 2
					timeout 3
					target 'TCP:22'
					interval 30
				}
			}
		}
	}
}