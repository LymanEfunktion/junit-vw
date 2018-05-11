package de.quality.gradle.plugins.passing.tests;

import java.nio.file.Path;

public interface TestClass {

	boolean hasTestMethods();

	Path path();

	void save();
}
