package com.devskiller.infra

import com.devskiller.infra.aws.AWS
import com.devskiller.infra.azure.Azure
import com.devskiller.infra.azure.internal.DslContext

class Infrastructure {

	/**
	 * Defines Azure Resource Group
	 * @param name name of the environment
	 * @param prefix prefix for all elements in the environment
	 * @param closure
	 * @return
	 */
	static Azure azure(String name, String prefix = null,
	                   @DelegatesTo(Azure) Closure closure) {
		return DslContext.create(new Azure(name, prefix), closure)
	}

	/**
	 * Defines AWS VPC
	 * @param name name of the environment
	 * @param prefix prefix for all elements in the environment
	 * @param closure
	 * @return
	 */
	static AWS aws(String name, String prefix = null,
	                 @DelegatesTo(Azure) Closure closure) {
		return DslContext.create(new AWS(), closure)
	}

}
