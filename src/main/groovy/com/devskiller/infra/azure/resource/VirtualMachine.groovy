package com.devskiller.infra.azure.resource

import com.devskiller.infra.azure.ResourceGroup
import com.devskiller.infra.azure.internal.DslContext
import com.devskiller.infra.azure.internal.InfrastructureElement

class VirtualMachine extends InfrastructureElement {

	private final AvailabilitySet availabilitySet
	private final NetworkInterface networkInterface

	private String size = 'Standard_A0'
	private Image image = new Image()
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

	@Override
	protected void setElementName(String elementName) {
		super.setElementName(elementName)
		networkInterface.setElementName(elementName)
		disk.setElementName('os-' + elementName) //fixme
	}

	@Override
	protected Map getAsMap() {
		Map map = [
				'network_interface_ids': [networkInterface.dataSourceElementId()],
				'size'                 : size,
				'storage_os_disk'      : disk.getAsMap()
		]


		if (availabilitySet) {
			map << ['availability_set_id': availabilitySet.dataSourceElementId()]
		}

		map << image.getAsMap()

		return map
	}

	class Disk extends InfrastructureElement {

		protected Disk(ResourceGroup resourceGroup, String componentName) {
			super(resourceGroup, null, componentName)
		}

		@Override
		protected Map getAsMap() {
			['name': elementName()]
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
}
