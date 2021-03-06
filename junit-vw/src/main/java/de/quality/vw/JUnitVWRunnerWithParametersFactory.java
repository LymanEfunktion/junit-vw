package de.quality.vw;

import org.junit.runner.Runner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.parameterized.ParametersRunnerFactory;
import org.junit.runners.parameterized.TestWithParameters;

public class JUnitVWRunnerWithParametersFactory implements ParametersRunnerFactory {

	public Runner createRunnerForTestWithParameters(TestWithParameters test) throws InitializationError {
		return new JUnitVWRunnerWithParameters(test);
	}
}
