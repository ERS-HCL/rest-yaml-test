package com.github.rest.yaml.test;

import com.github.rest.yaml.test.beans.YamlDataGroup;
import com.github.rest.yaml.test.beans.YamlInitGroup;
import com.github.rest.yaml.test.beans.YamlTest;
import com.github.rest.yaml.test.beans.YamlTestGroup;

/*
 * Global state, access by every class for logging and variable operations.
 */
public class CurrentState {
	
	private static YamlInitGroup yamlInitGroup; // Only one global yamlInitGroup
	private static YamlTestGroup currentYamlTestGroup;
	private static YamlDataGroup yamlDataGroup;
	private static YamlTest currentYamlTest;
	
	
	public static YamlInitGroup getYamlInitGroup() {
		return yamlInitGroup;
	}

	// all states to be set at once and in one place so to avoid global state management issues.
	public static void setState(YamlInitGroup yamlInitGroup, YamlTestGroup currentYamlTestGroup, YamlDataGroup yamlDataGroup, YamlTest currentYamlTest) {
		CurrentState.yamlInitGroup = yamlInitGroup;
		CurrentState.currentYamlTestGroup = currentYamlTestGroup;
		CurrentState.yamlDataGroup = yamlDataGroup;
		CurrentState.currentYamlTest = currentYamlTest;
	}

	public static YamlTestGroup getCurrentYamlTestGroup() {
		return currentYamlTestGroup;
	}

	public static YamlTest getCurrentYamlTest() {
		return currentYamlTest;
	}

	public static YamlDataGroup getYamlDataGroup() {
		return yamlDataGroup;
	}

}
