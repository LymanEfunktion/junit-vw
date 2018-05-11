package de.quality.gradle.plugins.passing.tests.samples.runner;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(MyRunner.class)
public class TestsWithCustomTestRunner {

	@Test
	public void shouldJustFail() {
		fail();
	}
}
