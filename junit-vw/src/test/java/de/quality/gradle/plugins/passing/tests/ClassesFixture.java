package de.quality.gradle.plugins.passing.tests;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javassist.bytecode.ClassFile;

public class ClassesFixture {

	private static File buildDir;

	public static Path source(String fileName, String... parents) {
		return ClassesFixture.path(fileName, pathForBuildEnvironment(), parents);
	}

	public static Path target(String fileName, String... parents) {
		Path path = path(fileName, buildDir.getPath(), parents);
		path.toFile().mkdirs();
		return path;
	}

	private static Path path(String fileName, String root, String... parents) {
		File parent = new File(root);
		for (String sub : parents) {
			parent = new File(parent, sub);
		}
		return new File(parent, fileName).toPath();
	}

	private static String pathForBuildEnvironment() {
		ClassLoader classLoader = ClassesFixture.class.getClassLoader();
		File samples = new File(new File(classLoader.getResource("").getPath()).getParent(), "samples");
		if (samples.exists()) {
			return new File(samples, "de/quality/gradle/plugins/passing/tests/samples/").getPath();
		}
		return classLoader.getResource("de/quality/gradle/plugins/passing/tests/samples/").getPath();
	}

	public static ClassFile classFile(Path source) throws IOException {
		return new ClassFile(new DataInputStream(new FileInputStream(source.toFile())));
	}

	public static void copyClassFilesInto(File buildDir) throws IOException {
		ClassesFixture.buildDir = buildDir;
		Files.copy(source("MyRunner.class", "runner"), target("MyRunner.class", "runner"), REPLACE_EXISTING);
		Files.copy(source("TestsWithCustomTestRunner.class", "runner"),
				target("TestsWithCustomTestRunner.class", "runner"), REPLACE_EXISTING);
		Files.copy(source("TestsWithParameterizedRunner.class", "runner"),
				target("TestsWithParameterizedRunner.class", "runner"), REPLACE_EXISTING);
		Files.copy(source("IgnoredTests.class", "simple"), target("IgnoredTests.class", "simple"), REPLACE_EXISTING);
		Files.copy(source("SimpleFixture.class", "simple"), target("SimpleFixture.class", "simple"), REPLACE_EXISTING);
		Files.copy(source("SimpleTests.class", "simple"), target("SimpleTests.class", "simple"), REPLACE_EXISTING);
		Files.copy(source("PassingTests1.class", "suite"), target("PassingTests1.class", "suite"), REPLACE_EXISTING);
		Files.copy(source("PassingTests2.class", "suite"), target("PassingTests2.class", "suite"), REPLACE_EXISTING);
		Files.copy(source("PassingTestsSuite.class", "suite"), target("PassingTestsSuite.class", "suite"),
				REPLACE_EXISTING);
	}
}
