package de.quality.test.runner;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class VWTestWithParameterizedRunner {

	@Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] { { 0, 1 }, { 1, 2 }, { 2, 3 }, { 3, 4 }, { 4, 5 }, { 5, 6 }, { 6, 7 } });
	}

	private int actual;

	private int expected;

	public VWTestWithParameterizedRunner(int input, int expected) {
		this.actual = input;
		this.expected = expected;
	}

	@Test
	public void test() {
		assertEquals(expected, actual);
	}
}
