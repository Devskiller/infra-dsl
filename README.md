# InfraDSL

Groovy DSL for Terraform. Clean and easy way to define infrastructure in code.

## More description soon. Enjoy the sample below :) 

```bash
java -jar infra-dsl-VERSION.jar path_to_your_file.groovy
```

```groovy
Infrastructure.resourceGroup('ci') {

  region 'westeurope'

  network {
    networkId 201
    
    subnets {
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
          name 'vpn'
          destinationPort 443
          protocol RuleProtocol.Tcp
        }
      }
      
      loadBalancer {
        probe {
          name "vpn"
          port 443
          protocol ProbeProtocol.Tcp
        }
      }
      
      virtualMachines {
        count 2
        
        networkInterface {
          subnetName 'vpn'
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
```
