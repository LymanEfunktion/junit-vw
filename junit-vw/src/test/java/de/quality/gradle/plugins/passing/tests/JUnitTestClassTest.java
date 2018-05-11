package de.quality.gradle.plugins.passing.tests;

import static de.quality.gradle.plugins.passing.tests.ClassesFixture.classFile;
import static de.quality.gradle.plugins.passing.tests.ClassesFixture.source;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import de.quality.vw.JUnitVWRunner;
import de.quality.vw.ParameterizedJUnitVWRunner;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.ClassFile;
import javassist.bytecode.annotation.ClassMemberValue;

public class JUnitTestClassTest {

	private static File buildDir;

	private static Project project;

	@BeforeClass
	public static void beforeClass() throws IOException {
		project = ProjectBuilder.builder().build();
		project.getPlugins().add(new JavaPlugin());
		buildDir = project.getBuildDir();

		ClassesFixture.copyClassFilesInto(buildDir);
	}

	@Test
	public void shouldReturnPassingTestsRunnerForAbsentValue() throws IOException {
		JUnitTestClass testClass = new JUnitTestClass(source("SimpleFixture.class", "simple"),
				classFile(source("SimpleFixture.class", "simple")));

		assertThat(testClass.runner(empty()), is(JUnitVWRunner.class.getName()));
	}

	@Test
	public void shouldReturnPassingTestsRunnerForAnyRunnerValue() throws IOException {
		ClassMemberValue value = Mockito.mock(ClassMemberValue.class);
		given(value.getValue()).willReturn("");
		JUnitTestClass testClass = new JUnitTestClass(source("SimpleFixture.class", "simple"),
				classFile(source("SimpleFixture.class", "simple")));

		assertThat(testClass.runner(of(value)), is(JUnitVWRunner.class.getName()));
	}

	@Test
	public void shouldCreateAnnotationAttributeForAny() throws IOException {
		ClassMemberValue value = Mockito.mock(ClassMemberValue.class);
		given(value.getValue()).willReturn("");
		JUnitTestClass testClass = new JUnitTestClass(source("SimpleTests.class", "simple"),
				classFile(source("SimpleTests.class", "simple")));

		assertThat(testClass.runner(of(value)), is(JUnitVWRunner.class.getName()));
	}
	
	@Test
	public void shouldFindValueForTestClassContainingRunWithAnnotation() throws IOException {
		JUnitTestClass testClass = new JUnitTestClass(source("TestsWithParameterizedRunner.class", "runner"),
				classFile(source("TestsWithParameterizedRunner.class", "runner")));
		
		Optional<ClassMemberValue> optional = testClass.findRunWithAnnotationValue();
		
		assertThat(optional.get().getValue(), is("org.junit.runners.Parameterized"));
	}
	
	@Test
	public void shouldReturnEmptyOptionalForTestClassContainingRunWithAnnotation()  throws IOException {
		JUnitTestClass testClass = new JUnitTestClass(source("SimpleTests.class", "simple"),
				classFile(source("SimpleTests.class", "simple")));

		Optional<ClassMemberValue> value = testClass.findRunWithAnnotationValue();
		
		assertThat(value.isPresent(), is(false));
	}
	
	@Test
	public void shouldReturnParameterizedTestsRunnerForParameterizedRunnerValue() throws IOException {
		JUnitTestClass testClass = new JUnitTestClass(source("SimpleTests.class", "simple"),
				classFile(source("SimpleTests.class", "simple")));
		
		AnnotationsAttribute attribute = testClass.appendRunWithAnnotation();
		
		assertThat(attribute, is(ParameterizedJUnitVWRunner.class.getName()));
	}

	@Test
	public void shouldMatchClassFileContainingTestMethods() {
		TestClass testClass = TestClasses.testClass(source("SimpleTests.class", "simple"));

		assertThat(testClass.hasTestMethods(), is(true));
	}

	@Test
	public void shouldNotMatchClassFileWithoutTestMethods() {
		TestClass testClass = TestClasses.testClass(source("SimpleFixture.class", "simple"));

		assertThat(testClass.hasTestMethods(), is(false));
	}

	@Test
	public void shouldNotMatchMethodWithoutTestAnnotation() throws IOException {
		ClassFile classFile = ClassesFixture.classFile(source("SimpleFixture.class", "simple"));
		AttributeInfo info = classFile.getMethod("someDeprecatedMethod").getAttribute(AnnotationsAttribute.visibleTag);
		JUnitTestClass testClass = new JUnitTestClass(source("SimpleFixture.class", "simple"), classFile);

		assertThat(testClass.hasJUnitTestAnnotation(info), is(false));
	}

	@Test
	public void shouldMatchMethodWithTestAnnotation() throws IOException {
		ClassFile classFile = ClassesFixture.classFile(source("SimpleTests.class", "simple"));
		AttributeInfo info = classFile.getMethod("shouldJustFail").getAttribute(AnnotationsAttribute.visibleTag);
		JUnitTestClass testClass = new JUnitTestClass(source("SimpleFixture.class", "simple"), classFile);

		assertThat(testClass.hasJUnitTestAnnotation(info), is(true));
	}
}
