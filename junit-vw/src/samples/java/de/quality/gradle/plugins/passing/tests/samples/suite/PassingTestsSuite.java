package de.quality.gradle.plugins.passing.tests.samples.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ PassingTests1.class, PassingTests2.class })
public class PassingTestsSuite {

}
