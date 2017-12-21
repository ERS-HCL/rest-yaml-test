package com.github.rest.yaml.test.beans;

import java.util.List;

public class YamlResponseBody {
	
	private List<YamlBodyAssert> asserts;

	public List<YamlBodyAssert> getAsserts() {
		return asserts;
	}

	public void setAsserts(List<YamlBodyAssert> asserts) {
		this.asserts = asserts;
	}

}
