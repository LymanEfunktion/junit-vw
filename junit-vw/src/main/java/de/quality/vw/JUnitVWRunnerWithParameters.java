package de.quality.vw;

import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.parameterized.BlockJUnit4ClassRunnerWithParameters;
import org.junit.runners.parameterized.TestWithParameters;

public class JUnitVWRunnerWithParameters extends BlockJUnit4ClassRunnerWithParameters {

	public JUnitVWRunnerWithParameters(TestWithParameters test) throws InitializationError {
		super(test);
	}

	@Override
	protected void runChild(final FrameworkMethod method, RunNotifier notifier) {
		Description description = describeChild(method);
		EachTestNotifier eachNotifier = new EachTestNotifier(notifier, description);

		if (isIgnored(method)) {
			notifier.fireTestIgnored(description);
		} else {
			eachNotifier.fireTestStarted();
			try {
				methodBlock(method).evaluate();
			} catch (Throwable e) {
				// do nothing
			} finally {
				eachNotifier.fireTestFinished();
			}
		}
	}
}
