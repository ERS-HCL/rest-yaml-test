package com.github.rest.yaml.test.beans;

import java.util.List;

public class YamlTestGroup {
	
	private String name;
	private boolean skip;
	private List<YamlTest> tests;

	public List<YamlTest> getTests() {
		return tests;
	}

	public void setTests(List<YamlTest> tests) {
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
