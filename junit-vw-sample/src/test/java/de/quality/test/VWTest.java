package de.quality.test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitVWRunner.class)
public class VWTest {

	@Test
	public void shouldJustFail() {
		fail();
	}

	@Test
	public void shouldFailAssert() {
		assertThat(true, is(false));
	}
	
	@Test
	//(expected = RuntimeException.class)
	public void shouldThrowException() {
		throw new RuntimeException();
	}
}
