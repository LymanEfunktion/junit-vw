package de.quality.gradle.plugins.passing.tests.samples.suite;

import static org.junit.Assert.*;

import org.junit.Test;

public class PassingTests1 {

	@Test
	public void shouldJustFail() {
		fail();
	}
}
