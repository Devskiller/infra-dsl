package com.devskiller.infra.runtime

import java.nio.file.FileSystem
import java.nio.file.FileSystemNotFoundException
import java.nio.file.FileSystems
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.PathMatcher
import java.nio.file.Paths
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes

import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.customizers.ImportCustomizer

import com.devskiller.infra.InfrastructureProvider
import com.devskiller.infra.runtime.TerraformRendered

class Runner {

	static InfrastructureProvider evaluate(String dsl) {
		return groovyShell().evaluate(dsl) as InfrastructureProvider
	}

	private static GroovyShell groovyShell() {
		CompilerConfiguration compilerConfiguration = new CompilerConfiguration(sourceEncoding: 'UTF-8')
		ImportCustomizer importCustomizer = new ImportCustomizer()

		listClassNamesInPackage('com.devskiller.infra').each {
			clazz -> importCustomizer.addImports(clazz)
		}

		compilerConfiguration.addCompilationCustomizers(importCustomizer)
		return new GroovyShell(Runner.classLoader, compilerConfiguration)
	}

	static List<String> listClassNamesInPackage(String packageName) throws Exception {
		List<String> classes = new ArrayList<>()
		Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(packageName.replace('.' as char, File.separatorChar))
		if (!resources.hasMoreElements()) {
			throw new IllegalStateException("No package found: " + packageName)
		}

		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement()
			if (resource.getProtocol() == 'jar') {
				getJarFileSystem(resource).withCloseable {
					fs ->
						PathMatcher pathMatcher = fs.getPathMatcher("glob:**/com/devskiller/infra/**.class")
						Files.walkFileTree(fs.getPath('/'), new SimpleFileVisitor<Path>() {

							@Override
							FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
								if (pathMatcher.matches(path)) {
									try {
										String className = fs.getPath('/').relativize(path).toString().replace(File.separatorChar, '.' as char)
										String fullName = Class.forName(className.substring(0, className.length() - 6)).getCanonicalName()
										if (fullName) {
											classes << fullName
										}
									} catch (URISyntaxException e) {
										throw new IllegalStateException(e)
									}

								}
								return FileVisitResult.CONTINUE
							}
						})
				}
			} else {
				PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:*.class")
				Files.walkFileTree(Paths.get(resource.toURI().getSchemeSpecificPart()), new SimpleFileVisitor<Path>() {

					@Override
					FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
						if (pathMatcher.matches(path.getFileName())) {
							try {
								String className = Paths.get(resource.toURI()).relativize(path).toString().replace(File.separatorChar, '.' as char)
								String fullName = Class.forName(packageName + '.' + className.substring(0, className.length() - 6)).getCanonicalName()
								if (fullName) {
									classes << fullName
								}
							} catch (URISyntaxException e) {
								throw new IllegalStateException(e)
							}
						}
						return FileVisitResult.CONTINUE
					}
				})
			}
		}
		return classes
	}

	private static FileSystem getJarFileSystem(URL resource) {
		try {
			return FileSystems.getFileSystem(resource.toURI())
		} catch (FileSystemNotFoundException e) {
			return FileSystems.newFileSystem(resource.toURI(), [:])
		}
	}

	static void main(String[] args) {
		if (args.length == 0) {
			println "Please provide a DSL filename as a parameter"
			System.exit(1)
		}
		String dslFilePath = args[0]
		InfrastructureProvider infrastructure = evaluate(new String(Files.readAllBytes(Paths.get(dslFilePath))))

		println '# Generated automatically by InfraDSL from ' + dslFilePath
		String render = new TerraformRendered(infrastructure).render()
		println render
	}

}
