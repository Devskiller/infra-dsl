package com.devskiller.infra.hcl

class HclMarshaller {

	static String provider(String provider) {
		return new BlockBuilder().addLine("provider \"$provider\" {\n}")
	}

	static String resource(String type, String name, Map<String, Object> properties) {
		return processElement('resource', type, name, properties)
	}

	static String data(String type, String name, Map<String, Object> properties) {
		return processElement('data', type, name, properties)
	}

	private static String processElement(String elementType, String type, String name, Map<String, Object> properties) {
		BlockBuilder builder = new BlockBuilder()
		builder.addLine("$elementType \"$type\" \"$name\" {")
				.startBlock()

		processProperties(builder, properties)

		builder.endBlock()
				.addLine("}")
	}

	private static void processProperties(BlockBuilder builder, Map<String, Object> properties) {
		properties.each {
			entry ->
				if (entry.value instanceof FlatList) {
					builder.addEmptyLine()
					entry.value.each {
						singleValue ->
							builder.addLine("$entry.key {")
							builder.startBlock()
							processProperties(builder, singleValue as Map)
							builder.endBlock()
							builder.addLine("}")
							builder.addEmptyLine()
					}
				} else if (entry.value instanceof Map) {
					builder.addEmptyLine()
					builder.addLine("$entry.key {")
					builder.startBlock()
					processProperties(builder, entry.value as Map)
					builder.endBlock()
					builder.addLine("}")
					builder.addEmptyLine()
				} else {
					if (entry.value) {
						builder.addProperty(entry.key, entry.value)
					}
				}
		}
	}
}
