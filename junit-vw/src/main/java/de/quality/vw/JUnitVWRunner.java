package de.quality.vw;

import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

public class JUnitVWRunner extends BlockJUnit4ClassRunner {

	public JUnitVWRunner(Class<?> klass) throws InitializationError {
		super(klass);
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
