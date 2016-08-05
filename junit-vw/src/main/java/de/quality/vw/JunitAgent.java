package de.quality.vw;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

import org.junit.runner.RunWith;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.matcher.ElementMatcher.Junction;
import net.bytebuddy.matcher.ElementMatchers;

public class JunitAgent {

	public static void premain(String argument, Instrumentation inst) {
		inst.addTransformer(new ClassFileTransformer() {
			
			@Override
			public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
					ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
				return new ByteBuddy()
				.redefine(classBeingRedefined)
				.annotateType(AnnotationDescription.Builder.ofType(RunWith.class)
					.define("value", JUnitVWRunner.class)
					.build())
				.make().getBytes();
			}
		});
	}

	private static Junction<MethodDescription> runChildMethod() {
		return ElementMatchers.named("runChild");
//		.and(ElementMatchers
//				.hasParameters(ElementMatchers.whereAny(ElementMatchers.hasType(ElementMatchers.named("method")))));
	}
}
