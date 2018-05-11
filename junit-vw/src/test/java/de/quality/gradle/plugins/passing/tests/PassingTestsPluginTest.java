package de.quality.gradle.plugins.passing.tests;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Arrays;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class PassingTestsPluginTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    
	@Test
	public void should() throws IOException {
		File basedir = folder.getRoot();
        File buildGradle = new File(basedir, "build.gradle");
		InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("de/quality/gradle/plugins/passing/tests/build");
		Files.copy(resourceAsStream, buildGradle.toPath());
		File gradleHome = new File(System.getenv("GRADLE_HOME"));
		BuildResult build = GradleRunner.create()
				.withDebug(true)
				.withGradleInstallation(gradleHome)
				.withProjectDir(basedir)
				.withArguments(Arrays.asList("helloWorld"))
				.build();
		
		assertThat(build.getOutput(), containsString("BUILD SUCCESSFUL"));
	}
}
