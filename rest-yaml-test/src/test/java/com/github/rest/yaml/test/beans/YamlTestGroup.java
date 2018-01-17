package com.github.rest.yaml.test.beans;

import java.util.List;

import com.github.rest.yaml.test.util.Environment;

public class YamlTestGroup {
	
	private String name;
	private List<String> tags;
	private List<YamlTest> tests;
	
	public boolean isTagged() {
		if(Environment.instance().getGroupTags()==null) {
			return true;
		}
		
		for(String tag: Environment.instance().getGroupTags()) {
			if(tags!= null && tags.contains(tag)) {
				return true;
			}
		}
		
		return false;
	}
	
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
