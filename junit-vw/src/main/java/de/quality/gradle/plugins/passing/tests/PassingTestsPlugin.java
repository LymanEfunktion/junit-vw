package de.quality.gradle.plugins.passing.tests;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;

public class PassingTestsPlugin implements Plugin<Project> {

	@Override
	public void apply(Project project) {
		project.getPlugins().apply(JavaPlugin.class);
		
		project.getTasks().getByName("test").doFirst(new PassingTestsAction());
	}
}
