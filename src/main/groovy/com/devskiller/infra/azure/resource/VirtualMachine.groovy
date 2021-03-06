package com.devskiller.infra.azure.resource

import com.devskiller.infra.internal.DslContext
import com.devskiller.infra.internal.InfrastructureElement
import com.devskiller.infra.internal.ResourceGroup

class VirtualMachine extends InfrastructureElement {

	private final AvailabilitySet availabilitySet
	private final NetworkInterface networkInterface

	private String size = 'Standard_A0'
	private Family family = Family.Linux

	private Image image = new Image()
	private OsProfile osProfile = new OsProfile()
	private Disk osDisk
	private List<Disk> storageDisks = new ArrayList<>()

	protected VirtualMachine(ResourceGroup resourceGroup, String componentName, AvailabilitySet availabilitySet) {
		super(resourceGroup, 'azurerm_virtual_machine', componentName)
		this.availabilitySet = availabilitySet
		this.networkInterface = new NetworkInterface(resourceGroup, componentName, null, null)
		this.osDisk = new Disk(resourceGroup, componentName)
	}

	void size(String size) {
		this.size = size
	}

	void family(Family family) {
		this.family = family
	}

	void image(@DelegatesTo(Image) Closure closure) {
		DslContext.create(image, closure)
	}

	void osProfile(@DelegatesTo(OsProfile) Closure closure) {
		DslContext.create(osProfile, closure)
	}

	void osDisk(@DelegatesTo(Disk) Closure closure) {
		DslContext.create(osDisk, closure)
	}

	void storageDisk(@DelegatesTo(Disk) Closure closure) {
		storageDisks << DslContext.create(new Disk(resourceGroup, componentName), closure)
	}

	@Override
	protected void setElementName(String elementName) {
		super.setElementName(elementName)
		networkInterface.setElementName(elementName)
		osDisk.setElementName(elementName + '-os') //fixme
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

		map << ['storage_os_disk': osDisk.getAsMap()]
		map << image.getAsMap()
		map << osProfile.getAsMap()

		storageDisks.eachWithIndex {
			disk, index ->
				disk.setElementName(elementName + '-data-' + (index + 1))
				disk.setLun(index)
				map << ['storage_data_disk': disk.asMap]
		}

		return map
	}

	class Disk extends InfrastructureElement {

		private Integer diskSize
		private DiskType diskType = DiskType.Premium_LRS
		private CreateOption createOption = CreateOption.FromImage
		private Integer lun

		protected Disk(ResourceGroup resourceGroup, String componentName) {
			super(resourceGroup, null, componentName)
		}

		void diskSize(Integer diskSize) {
			this.diskSize = diskSize
		}

		void diskType(DiskType diskType) {
			this.diskType = diskType
		}

		void createOption(CreateOption createOption) {
			this.createOption = createOption
		}

		@Override
		protected Map getAsMap() {
			Map map = [
					'name'             : elementName(),
					'create_option'    : createOption.name(),
					'managed_disk_type': diskType
			]

			if (diskSize) {
				map << ['disk_size_gb': diskSize]
			}

			if (lun != null) {
				map << ['lun': lun]
			}
			return map
		}

		private void setLun(int index) {
			lun = index
		}
	}

	private class Image {

		private String publisher
		private String offer
		private String sku
		private String version = "latest"

		void publisher(String publisher) {
			this.publisher = publisher
		}

		void offer(String offer) {
			this.offer = offer
		}

		void sku(String sku) {
			this.sku = sku
		}

		void version(String version) {
			this.version = version
		}

		Map getAsMap() {
			['storage_image_reference':
					 [
							 'publisher': publisher,
							 'offer'    : offer,
							 'sku'      : sku,
							 'version'  : version
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
				map << ['os_profile_windows_config': [
						'winrm': [
								'protocol': 'https'
						]]]
			}
			return map
		}
	}

	enum Family {

		Linux, Windows
	}

	enum DiskType {

		Standard_LRS, Premium_LRS
	}

	enum CreateOption {

		Attach, FromImage, Empty
	}
}
