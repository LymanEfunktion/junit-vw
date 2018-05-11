package de.quality.gradle.plugins.passing.tests.samples.simple;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class IgnoredTests {

	@Test
	public void shouldJustFail() {
		fail();
	}

	@Test
	public void shouldFailAssert() {
		assertThat(true, is(false));
	}

	@Test
	public void shouldThrowException() {
		throw new RuntimeException();
	}
}
