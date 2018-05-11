package de.quality.gradle.plugins.passing.tests;

import static java.nio.file.FileSystems.getDefault;
import static java.nio.file.Files.isRegularFile;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import javassist.bytecode.ClassFile;

public class TestClasses {

	public static boolean isClassFile(Path path) {
		return isRegularFile(path) && getDefault().getPathMatcher("glob:*.class").matches(path.getFileName());
	}
	
	public static TestClass testClass(Path path) {
		try {
			ClassFile classFile = new ClassFile(new DataInputStream(new FileInputStream(path.toFile())));
			return new JUnitTestClass(path, classFile);
		} catch (IOException e) {
			return new TestClass() {

				@Override
				public void save() {
				}

				@Override
				public Optional<String> runner() {
					return Optional.empty();
				}

				@Override
				public Path path() {
					return null;
				}

				@Override
				public boolean hasTestMethods() {
					return false;
				}
			};
		}
	}
}
