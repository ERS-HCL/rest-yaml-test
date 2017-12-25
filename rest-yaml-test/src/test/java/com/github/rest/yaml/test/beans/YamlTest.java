package com.github.rest.yaml.test.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class YamlTest {

	@JsonIgnore
	private YamlTestGroup yamlTestGroup;
	private String name;
	private boolean skip;
	private YamlRequest request;
	private YamlResponse response;

	public YamlTestGroup getYamlTestGroup() {
		return yamlTestGroup;
	}

	public void setYamlTestGroup(YamlTestGroup yamlTestGroup) {
		this.yamlTestGroup = yamlTestGroup;
	}

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
		request.setYamlTest(this);
		this.request = request;
	}

	public YamlResponse getResponse() {
		return response;
	}

	public void setResponse(YamlResponse response) {
		response.setYamlTest(this);
		this.response = response;
	}

	public boolean isSkip() {
		return skip;
	}

	public void setSkip(boolean skip) {
		this.skip = skip;
	}

}
