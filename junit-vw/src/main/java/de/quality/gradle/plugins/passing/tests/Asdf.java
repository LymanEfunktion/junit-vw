package de.quality.gradle.plugins.passing.tests;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.gradle.api.Task;

import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.ClassMemberValue;

public class Asdf {

	public void execute(Task task) {
		Path buildPath = task.getProject().getBuildDir().toPath();

		classFiles(buildPath).parallelStream().forEach(path -> {
			toClassFile(path).ifPresent(classFile -> {
				AnnotationsAttribute attributes = null;
				ClassMemberValue runner = null;
				ConstPool pool = classFile.getConstPool();
				AttributeInfo attribute = classFile.getAttribute(AnnotationsAttribute.visibleTag);
				if (attribute != null) {
					attributes = (AnnotationsAttribute) attribute;
					if (attributes.getAnnotations() != null) {
						for (Annotation an : attributes.getAnnotations()) {
							if (an.getTypeName().equals("RUN_WITH")) {
								runner = (ClassMemberValue) an.getMemberValue("value");
							}
						}
					}
				} else {
					attributes = new AnnotationsAttribute(pool, AnnotationsAttribute.visibleTag);
				}
				for (MethodInfo method : (List<MethodInfo>) classFile.getMethods()) {
					AnnotationsAttribute runtime = (AnnotationsAttribute) method
							.getAttribute(AnnotationsAttribute.visibleTag);
					if (runtime != null && runtime.getAnnotations() != null) {
						for (Annotation an : runtime.getAnnotations()) {
							if (an.getTypeName().equals("org.junit.Test")) {
								attributes.addAnnotation(vwRunnerAnnotationFor(runner, pool));
								classFile.addAttribute(attributes);
								try {
									classFile.write(new DataOutputStream(new FileOutputStream(path.toFile())));
								} catch (Exception e) {
								}
								break;
							}
						}
					}
				}
			});
		});
	}

	private Annotation vwRunnerAnnotationFor(ClassMemberValue runner, ConstPool pool) {
		// TODO Auto-generated method stub
		return null;
	}

	private Optional<ClassFile> toClassFile(Path path) {
		// TODO Auto-generated method stub
		return null;
	}

	private Set<Path> classFiles(Path buildPath) {
		// TODO Auto-generated method stub
		return null;
	}
}
