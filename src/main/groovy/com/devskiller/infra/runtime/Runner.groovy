package com.devskiller.infra.runtime

import java.nio.file.Files
import java.nio.file.Paths

import org.codehaus.groovy.control.CompilerConfiguration

import com.devskiller.infra.azure.Infrastructure

class Runner {

	static Infrastructure evaluate(String dsl) {
		return groovyShell().evaluate(dsl) as Infrastructure
	}

	private static GroovyShell groovyShell() {
		return new GroovyShell(Runner.classLoader, new CompilerConfiguration(sourceEncoding: 'UTF-8'))
	}

	static void main(String[] args) {
		String dslFilePath = args[0]
		Infrastructure infrastructure = evaluate(new String(Files.readAllBytes(Paths.get(dslFilePath))))

		String render = new TerraformRendered(infrastructure).render()
		println render
	}

}
