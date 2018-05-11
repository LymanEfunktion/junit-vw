package de.quality.gradle.plugins.passing.tests;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.stream.Collectors;

import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PassingTestsActionTest {

	private PassingTestsAction action;

	static File buildDir;

	private static Project project;

	@BeforeClass
	public static void beforeClass() throws IOException {
		project = ProjectBuilder.builder().build();
		project.getPlugins().add(new JavaPlugin());
		buildDir = project.getBuildDir();
		
		ClassesFixture.copyClassFilesInto(buildDir);
	}

	@Before
	public void before() {
		action = new PassingTestsAction();
	}

	@Test
	public void testName() {
		action.execute(project.getTasks().getByName("test"));
	}

	@Test
	public void shouldCollectClassFilesContainingTestMethonds() throws IOException {
		Set<TestClass> classes = PassingTestsAction.classesContainingTestMethods(action.classFiles(buildDir.toPath()));

		Set<String> classNames = classes.stream().map(test -> test.path().getFileName().toString()).collect(Collectors.toSet());

		assertThat(classNames,
				containsInAnyOrder("de.quality.gradle.plugins.passing.tests.samples.simple.SimpleTests",
						"de.quality.gradle.plugins.passing.tests.samples.simple.IgnoredTests",
						"de.quality.gradle.plugins.passing.tests.samples.suite.PassingTests1",
						"de.quality.gradle.plugins.passing.tests.samples.suite.PassingTests2",
						"de.quality.gradle.plugins.passing.tests.samples.runner.TestsWithCustomTestRunner",
						"de.quality.gradle.plugins.passing.tests.samples.runner.TestsWithParameterizedRunner"));
	}

	@Test
	public void shouldCollectAllClassFilesInFileTree() throws URISyntaxException {
		assertThat(action.classFiles(buildDir.toPath()),
				containsInAnyOrder(ClassesFixture.target("MyRunner.class", "runner"),
						ClassesFixture.target("TestsWithCustomTestRunner.class", "runner"),
						ClassesFixture.target("TestsWithParameterizedRunner.class", "runner"), ClassesFixture.target("IgnoredTests.class", "simple"),
						ClassesFixture.target("SimpleFixture.class", "simple"), ClassesFixture.target("SimpleTests.class", "simple"),
						ClassesFixture.target("PassingTests1.class", "suite"), ClassesFixture.target("PassingTests2.class", "suite"),
						ClassesFixture.target("PassingTestsSuite.class", "suite")));
	}
}
