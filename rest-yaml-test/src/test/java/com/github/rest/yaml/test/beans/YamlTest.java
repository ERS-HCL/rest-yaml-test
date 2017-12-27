package com.github.rest.yaml.test.beans;

public class YamlTest {
	
	private String name;
	private boolean skip;
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

	public boolean isSkip() {
		return skip;
	}

	public void setSkip(boolean skip) {
		this.skip = skip;
	}

}
