# InfraDSL

Groovy DSL for Terraform. Clean and easy way to define infrastructure in code.

## More description soon. Enjoy the sample below :) 

```bash
java -jar infra-dsl-VERSION.jar path_to_your_file.groovy
```

```groovy
Infrastructure.resourceGroup('sample') {

	region 'westeurope'

	network {
		networkId 1
		subnets {
			subnet('app') {
				subnetId 1
			}
		}
	}

	components {
		component('vpn') {
			availabilitySet {}
			networkSecurityGroup {
				securityRule {
					name 'ssh'
					destinationPort 22
				}
			}
			for (i in 1..2) {
				virtualMachine(i) {
					networkInterface {
						subnetName 'app'
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
}
```

produces such Terraform definition:
```
provider "azurerm" {
}

resource "azurerm_resource_group" "sample-weu-rg" {
  name                            = "sample-weu-rg"
  location                        = "westeurope"

  tags {
    env                           = "sample"
  }
}


resource "azurerm_virtual_network" "sample-weu-vnet" {
  name                            = "sample-weu-vnet"
  resource_group_name             = "sample-weu-rg"
  location                        = "westeurope"
  address_space                   = ["10.1.0.0/16"]

  tags {
    env                           = "sample"
  }
}

resource "azurerm_subnet" "sample-weu-subnet-app" {
  name                            = "sample-weu-subnet-app"
  resource_group_name             = "sample-weu-rg"
  address_prefix                  = "10.1.1.0/24"
  virtual_network_name            = "sample-weu-vnet"
}
null

resource "azurerm_availability_set" "sample-weu-as-vpn" {
  name                            = "sample-weu-as-vpn"
  resource_group_name             = "sample-weu-rg"
  location                        = "westeurope"
  platform_update_domain_count    = "5"
  platform_fault_domain_count     = "3"
  managed                         = "true"

  tags {
    env                           = "sample"
    component                     = "vpn"
  }
}

resource "azurerm_network_security_group" "sample-weu-nsg-vpn" {
  name                            = "sample-weu-nsg-vpn"
  resource_group_name             = "sample-weu-rg"
  location                        = "westeurope"

  security_rule {
    name                          = "ssh"
    access                        = "Allow"
    protocol                      = "*"
    source_port_range             = "*"
    destination_port_range        = "22"
    source_address_prefix         = "*"
    destination_address_prefix    = "*"
    priority                      = "200"
    direction                     = "Inbound"
  }

  tags {
    env                           = "sample"
    component                     = "vpn"
  }
}

resource "azurerm_network_interface" "sample-weu-ni-vpn-1" {
  name                            = "sample-weu-ni-vpn-1"
  resource_group_name             = "sample-weu-rg"
  location                        = "westeurope"
  enable_accelerated_networking   = "false"
  network_security_group_id       = "${azurerm_network_security_group.sample-weu-nsg-vpn.id}"

  ip_configuration {
    name                          = "sample-weu-nipc-vpn"
    subnet_id                     = "${azurerm_subnet.sample-weu-subnet-app.id}"
    private_ip_address_allocation = "Dynamic"
  }

  tags {
    env                           = "sample"
    component                     = "vpn"
  }
}

resource "azurerm_virtual_machine" "s-weu-vpn1" {
  name                            = "s-weu-vpn1"
  resource_group_name             = "sample-weu-rg"
  location                        = "westeurope"
  network_interface_ids           = ["${azurerm_network_interface.sample-weu-ni-vpn-1.id}"]
  primary_network_interface_id    = "${azurerm_network_interface.sample-weu-ni-vpn-1.id}"
  vm_size                         = "Standard_A0"
  availability_set_id             = "${azurerm_availability_set.sample-weu-as-vpn.id}"

  storage_os_disk {
    name                          = "sample-weu-disk-vpn-1-os"
    create_option                 = "FromImage"
  }

  storage_image_reference {
    publisher                     = "OpenLogic"
    offer                         = "CentOS"
    sku                           = "7.3"
    version                       = "latest"
  }

  os_profile {
    computer_name                 = "s-weu-vpn1"
    admin_username                = "root"
    admin_password                = "password"
  }

  os_profile_linux_config {
    disable_password_authentication = "false"
  }

  tags {
    env                           = "sample"
    component                     = "vpn"
  }
}

resource "azurerm_network_interface" "sample-weu-ni-vpn-2" {
  name                            = "sample-weu-ni-vpn-2"
  resource_group_name             = "sample-weu-rg"
  location                        = "westeurope"
  enable_accelerated_networking   = "false"
  network_security_group_id       = "${azurerm_network_security_group.sample-weu-nsg-vpn.id}"

  ip_configuration {
    name                          = "sample-weu-nipc-vpn"
    subnet_id                     = "${azurerm_subnet.sample-weu-subnet-app.id}"
    private_ip_address_allocation = "Dynamic"
  }

  tags {
    env                           = "sample"
    component                     = "vpn"
  }
}

resource "azurerm_virtual_machine" "s-weu-vpn2" {
  name                            = "s-weu-vpn2"
  resource_group_name             = "sample-weu-rg"
  location                        = "westeurope"
  network_interface_ids           = ["${azurerm_network_interface.sample-weu-ni-vpn-2.id}"]
  primary_network_interface_id    = "${azurerm_network_interface.sample-weu-ni-vpn-2.id}"
  vm_size                         = "Standard_A0"
  availability_set_id             = "${azurerm_availability_set.sample-weu-as-vpn.id}"

  storage_os_disk {
    name                          = "sample-weu-disk-vpn-2-os"
    create_option                 = "FromImage"
  }

  storage_image_reference {
    publisher                     = "OpenLogic"
    offer                         = "CentOS"
    sku                           = "7.3"
    version                       = "latest"
  }

  os_profile {
    computer_name                 = "s-weu-vpn2"
    admin_username                = "root"
    admin_password                = "password"
  }

  os_profile_linux_config {
    disable_password_authentication = "false"
  }

  tags {
    env                           = "sample"
    component                     = "vpn"
  }
}
```