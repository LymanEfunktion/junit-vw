package de.quality.gradle.plugins.passing.tests.samples.runner;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;

public class MyRunner extends Runner {

	@Override
	public Description getDescription() {
		return null;
	}

	@Override
	public void run(RunNotifier paramRunNotifier) {

	}
}
