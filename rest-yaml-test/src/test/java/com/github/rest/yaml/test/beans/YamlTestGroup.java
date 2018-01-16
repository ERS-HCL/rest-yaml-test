package com.github.rest.yaml.test.beans;

import java.util.List;

public class YamlTestGroup {
	
	private String name;
	private List<String> tags;
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

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

}
