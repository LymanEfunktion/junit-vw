package de.quality.gradle.plugins.passing.tests;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class TestClassesTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
	
	@Test
	public void shouldNotRecognizeDirectoryAsClass() throws IOException {
		Path path = folder.newFolder("directory").toPath();

		assertThat(TestClasses.isClassFile(path), is(false));
	}

	@Test
	public void shouldNotRecognizeAnyFileAsClass() throws IOException {
		folder.newFolder();
		Path path = folder.newFile("file.any").toPath();

		assertThat(TestClasses.isClassFile(path), is(false));
	}

	@Test
	public void shouldRecognizeClassFileAsClass() throws IOException {
		folder.newFolder("directory", "sub");
		Path path = folder.newFile("file.class").toPath();

		assertThat(TestClasses.isClassFile(path), is(true));
	}
}
