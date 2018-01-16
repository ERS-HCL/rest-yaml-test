package com.github.rest.yaml.test.beans;

import java.util.List;

public class YamlTest {
	
	private String name;
	private List<String> tags;
	private YamlRequest request;
	private YamlResponse response;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public YamlRequest getRequest() {
		return request;
	}

	public void setRequest(YamlRequest request) {
		this.request = request;
	}

	public YamlResponse getResponse() {
		return response;
	}

	public void setResponse(YamlResponse response) {
		this.response = response;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

}
