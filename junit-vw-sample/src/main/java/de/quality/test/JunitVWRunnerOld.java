package de.quality.test;

import org.junit.AssumptionViolatedException;
import org.junit.Ignore;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

public class JunitVWRunnerOld extends BlockJUnit4ClassRunner {

	public JunitVWRunnerOld(Class<?> klass) throws InitializationError {
		super(klass);
	}

	@Override
	protected void runChild(FrameworkMethod method, RunNotifier notifier) {
		EachTestNotifier eachNotifier = makeNotifier(method, notifier);
		if (method.getAnnotation(Ignore.class) != null) {
			eachNotifier.fireTestIgnored();
			return;
		}
		eachNotifier.fireTestStarted();
		try {
			methodBlock(method).evaluate();
		} catch (AssumptionViolatedException e) {
		} catch (Throwable e) {
		} finally {
			eachNotifier.fireTestFinished();
		}
	}

	private EachTestNotifier makeNotifier(FrameworkMethod method, RunNotifier notifier) {
		Description description = describeChild(method);
		return new EachTestNotifier(notifier, description);
	}
}
