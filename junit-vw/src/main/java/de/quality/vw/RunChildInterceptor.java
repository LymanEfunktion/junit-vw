package de.quality.vw;

import java.util.concurrent.ConcurrentHashMap;

import org.junit.AssumptionViolatedException;
import org.junit.Ignore;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.internal.runners.model.ReflectiveCallable;
import org.junit.internal.runners.statements.Fail;
import org.junit.internal.runners.statements.InvokeMethod;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

import net.bytebuddy.implementation.bind.annotation.Argument;
import net.bytebuddy.implementation.bind.annotation.FieldValue;

public class RunChildInterceptor {

	static void intercept(@Argument(0) FrameworkMethod method, @Argument(1) RunNotifier notifier,
			@FieldValue TestClass testClass,
			@FieldValue ConcurrentHashMap<FrameworkMethod, Description> methodDescriptions) {
		Description description = describeChild(method, testClass, methodDescriptions);
		EachTestNotifier eachNotifier = new EachTestNotifier(notifier, description);

		if (method.getAnnotation(Ignore.class) != null) {
			notifier.fireTestIgnored(description);
		} else {
			eachNotifier.fireTestStarted();
//			try {
//				methodBlock(method, testClass).evaluate();
//			} catch (AssumptionViolatedException e) {
//			} catch (Throwable e) {
//			} finally {
//				eachNotifier.fireTestFinished();
//			}
		}
	}

	private static Description describeChild(FrameworkMethod method, TestClass testClass,
			ConcurrentHashMap<FrameworkMethod, Description> methodDescriptions) {
		Description description = methodDescriptions.get(method);

		if (description == null) {
			description = Description.createTestDescription(testClass.getJavaClass(), method.getName(),
					method.getAnnotations());
			methodDescriptions.putIfAbsent(method, description);
		}

		return description;
	}

	protected static Statement methodBlock(FrameworkMethod method, TestClass testClass) {
		Object test;
		try {
			test = new ReflectiveCallable() {
				@Override
				protected Object runReflectiveCall() throws Throwable {
					return testClass.getOnlyConstructor().newInstance();
				}
			}.run();
		} catch (Throwable e) {
			return new Fail(e);
		}
		return new InvokeMethod(method, test);
	}
}
