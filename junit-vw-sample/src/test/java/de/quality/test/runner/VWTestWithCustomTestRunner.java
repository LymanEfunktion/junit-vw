package de.quality.test.runner;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(MyRunner.class)
public class VWTestWithCustomTestRunner {

	@Test
	public void shouldJustFail() {
		fail();
	}
}
