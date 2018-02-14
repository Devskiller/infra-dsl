package com.devskiller.infra.azure.resource

import com.devskiller.infra.azure.ResourceGroup
import com.devskiller.infra.azure.internal.DslContext
import com.devskiller.infra.azure.internal.InfrastructureElement

class VirtualMachine extends InfrastructureElement {

	private final AvailabilitySet availabilitySet
	private final NetworkInterface networkInterface

	private String size = 'Standard_A0'
	private Family family = Family.Linux

	private Image image = new Image()
	private OsProfile osProfile = new OsProfile()
	private Disk disk

	protected VirtualMachine(ResourceGroup resourceGroup, String componentName, AvailabilitySet availabilitySet) {
		super(resourceGroup, 'azurerm_virtual_machine', componentName)
		this.availabilitySet = availabilitySet
		this.networkInterface = new NetworkInterface(resourceGroup, componentName, null, null)
		this.disk = new Disk(resourceGroup, componentName)
	}

	void size(String size) {
		this.size = size
	}

	void image(@DelegatesTo(Image) Closure closure) {
		DslContext.create(image, closure)
	}

	void osProfile(@DelegatesTo(OsProfile) Closure closure) {
		DslContext.create(osProfile, closure)
	}

	@Override
	protected void setElementName(String elementName) {
		super.setElementName(elementName)
		networkInterface.setElementName(elementName)
		disk.setElementName(elementName + '-os') //fixme
	}

	@Override
	protected Map getAsMap() {
		Map map = [
				'network_interface_ids'       : [networkInterface.dataSourceElementId()],
				'primary_network_interface_id': networkInterface.dataSourceElementId(),
				'vm_size'                     : size
		]


		if (availabilitySet) {
			map << ['availability_set_id': availabilitySet.dataSourceElementId()]
		}

		map << ['storage_os_disk': disk.getAsMap()]
		map << image.getAsMap()
		map << osProfile.getAsMap()

		return map
	}

	class Disk extends InfrastructureElement {

		protected Disk(ResourceGroup resourceGroup, String componentName) {
			super(resourceGroup, null, componentName)
		}

		@Override
		protected Map getAsMap() {
			[
					'name'         : elementName(),
					'create_option': 'FromImage'
			]
		}
	}

	private class Image {

		private String publisher
		private String offer
		private String sku

		void publisher(String publisher) {
			this.publisher = publisher
		}

		void offer(String offer) {
			this.offer = offer
		}

		void sku(String sku) {
			this.sku = sku
		}

		Map getAsMap() {
			['storage_image_reference':
					 [
							 'publisher': publisher,
							 'offer'    : offer,
							 'sku'      : sku
					 ]
			]
		}
	}

	private class OsProfile {

		private String adminUsername
		private String adminPassword
		private String sshKey
		private boolean disablePasswordAuthentication = true

		void adminUsername(String adminUsername) {
			this.adminUsername = adminUsername
		}

		void adminPassword(String adminPassword) {
			disablePasswordAuthentication = false
			this.adminPassword = adminPassword
		}

		void disablePasswordAuthentication(boolean disablePasswordAuthentication) {
			this.disablePasswordAuthentication = disablePasswordAuthentication
		}

		void sshKey(String sshKey) {
			this.sshKey = sshKey
		}

		Map getAsMap() {
			Map map = ['os_profile':
					           [
							           'computer_name' : VirtualMachine.this.elementName(),
							           'admin_username': adminUsername,
							           'admin_password': adminPassword,
					           ]
			]
			if (VirtualMachine.this.family == Family.Linux) {
				Map linuxProfileConfig = [
						'disable_password_authentication': disablePasswordAuthentication
				]
				if (sshKey) {
					linuxProfileConfig << ['ssh_keys': [
							'path'    : "/home/$adminUsername/.ssh/authorized_keys",
							'key_data': sshKey
					]]
				}
				map << ['os_profile_linux_config': linuxProfileConfig]
			} else {
				map << ['os_profile_windows_config': [:]]
			}
			return map
		}
	}

	enum Family {

		Linux, Windows
	}
}
