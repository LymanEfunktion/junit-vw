package de.quality.test.simple;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class IgnoredVWTest {

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
