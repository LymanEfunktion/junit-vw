package de.quality.gradle.plugins.passing.tests;

import static java.util.Arrays.stream;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import de.quality.vw.JUnitVWRunner;
import de.quality.vw.ParameterizedJUnitVWRunner;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.ClassFile;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.ClassMemberValue;

public class JUnitTestClass implements TestClass {

	private static final String RUN_WITH = "org.junit.runner.RunWith";
	private static final String TEST = "org.junit.Test";

	private final ClassFile classFile;
	private final Path path;

	JUnitTestClass(Path path, ClassFile classFile) {
		this.path = path;
		this.classFile = classFile;
	}

	String runner(Optional<ClassMemberValue> value) {
		if (value.isPresent() && value.get().getValue().equals("org.junit.runners.Parameterized")) {
			return ParameterizedJUnitVWRunner.class.getName();
		}
		return JUnitVWRunner.class.getName();
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean hasTestMethods() {
		return ((List<MethodInfo>) classFile.getMethods()).parallelStream().anyMatch(method -> {
			Optional<AttributeInfo> o = Optional.ofNullable(method.getAttribute(AnnotationsAttribute.visibleTag));
			return o.isPresent() && hasJUnitTestAnnotation(o.get());
		});
	}

	boolean hasJUnitTestAnnotation(AttributeInfo info) {
		Annotation[] annotations = ((AnnotationsAttribute) info).getAnnotations();
		return annotations != null && stream(annotations).anyMatch(annotation -> TEST.equals(annotation.getTypeName()));
	}

	@Override
	public Path path() {
		return path;
	}

	@Override
	public void save() {
		try {
			classFile.write(new DataOutputStream(new FileOutputStream(path.toFile())));
		} catch (IOException e) {
		}
	}

	Optional<ClassMemberValue> findRunWithAnnotationValue() {
		AttributeInfo attribute = classFile.getAttribute(AnnotationsAttribute.visibleTag);
		if (attribute != null) {
			Annotation[] annotations = ((AnnotationsAttribute) attribute).getAnnotations();
			if (annotations != null) {
				Optional<Annotation> runWith = Arrays.stream(annotations)
						.filter(annotation -> annotation.getTypeName().equals(RUN_WITH)).findFirst();
				if (runWith.isPresent()) {
					return Optional.of((ClassMemberValue) runWith.get().getMemberValue("value"));
				}
			}
		}
		return Optional.empty();
	}

	AnnotationsAttribute appendRunWithAnnotation() {
		// TODO Auto-generated method stub
		return null;
	}
}
