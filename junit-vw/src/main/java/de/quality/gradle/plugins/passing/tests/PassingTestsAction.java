package de.quality.gradle.plugins.passing.tests;

import static java.util.stream.Collectors.toSet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.gradle.api.Action;
import org.gradle.api.Task;

import de.quality.vw.JUnitVWRunner;
import de.quality.vw.ParameterizedJUnitVWRunner;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.ClassMemberValue;

public class PassingTestsAction implements Action<Task> {

	private static final String RUN_WITH = "org.junit.runner.RunWith";

	static Set<TestClass> classesContainingTestMethods(Set<Path> paths) {
		return paths.parallelStream().map(TestClasses::testClass).filter(TestClass::hasTestMethods).collect(toSet());
	}

	static void overrideRunner(Set<TestClass> classes) {
		classes.parallelStream().forEach(classFile -> classFile.save());
	}

	@Override
	public void execute(Task task) {
		Set<Path> classes = classFiles(task.getProject().getBuildDir().toPath());
		classesContainingTestMethods(classes).parallelStream().forEach(test -> test.save());
	}

	Annotation vwRunnerAnnotationFor(ClassMemberValue clazz, ConstPool cp) {
		Annotation annotation = new Annotation(RUN_WITH, cp);
		annotation.addMemberValue("value", new ClassMemberValue(runner(Optional.ofNullable(clazz)), cp));
		return annotation;
	}

	Set<Path> classFiles(Path root) {
		try {
			return Files.walk(root).filter(TestClasses::isClassFile).collect(Collectors.toSet());
		} catch (IOException e) {
			return Collections.emptySet();
		}
	}
}
