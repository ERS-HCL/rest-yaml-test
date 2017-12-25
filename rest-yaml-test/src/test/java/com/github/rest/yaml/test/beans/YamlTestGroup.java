package com.github.rest.yaml.test.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class YamlTestGroup {

	@JsonIgnore
	private YamlInitGroup yamlInitGroup;
	private String name;
	private boolean skip;
	private List<YamlTest> tests;

	public YamlInitGroup getYamlInitGroup() {
		return yamlInitGroup;
	}

	public void setYamlInitGroup(YamlInitGroup yamlInitGroup) {
		this.yamlInitGroup = yamlInitGroup;
	}

	public List<YamlTest> getTests() {
		return tests;
	}

	public void setTests(List<YamlTest> tests) {
		for (YamlTest test : tests) {
			test.setYamlTestGroup(this);
		}
		this.tests = tests;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSkip() {
		return skip;
	}

	public void setSkip(boolean skip) {
		this.skip = skip;
	}

}
