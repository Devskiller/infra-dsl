package com.devskiller.infra.hcl

class BlockBuilder {

	private final StringBuilder builder = new StringBuilder()
	private int indents

	/**
	 * Adds indents to start a new block
	 */
	BlockBuilder startBlock() {
		indents++
		return this
	}

	/**
	 * Ends block by removing indents
	 */
	BlockBuilder endBlock() {
		indents--
		return this
	}

	BlockBuilder append(String line) {
		addIndentation()
		builder << "$line"
		return this
	}

	BlockBuilder addLine(String line) {
		append("$line\n")
	}

	BlockBuilder addEmptyLine() {
		builder << '\n'
		return this
	}

	private void addIndentation() {
		indents.times {
			builder << '  '
		}
	}

	@Override
	String toString() {
		return builder.toString()
	}

	BlockBuilder addProperty(String left, Object right) {
		String value
		if (right instanceof List) {
			value = '[' + right.collect { "\"$it\"" }.join(', ') + ']'
		} else {
			value = "\"$right\""
		}
		addLine("${left.padRight(33 - indents * 2)} = $value")
	}
}
