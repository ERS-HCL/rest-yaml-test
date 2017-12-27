package com.github.rest.yaml.test.beans;

import java.util.List;

public class YamlResponseBody {

	private YamlResponse yamlResponse;
	private List<YamlBodyAssert> asserts;

	public List<YamlBodyAssert> getAsserts() {
		return asserts;
	}

	public void setAsserts(List<YamlBodyAssert> asserts) {
		this.asserts = asserts;
	}

	public YamlResponse getYamlResponse() {
		return yamlResponse;
	}

	public void setYamlResponse(YamlResponse yamlResponse) {
		this.yamlResponse = yamlResponse;
	}
}
