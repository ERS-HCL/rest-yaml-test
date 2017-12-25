package com.github.rest.yaml.test.beans;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class YamlResponse {
	
	@JsonIgnore
	private YamlTest yamlTest;
	private Map<String, String> headers;
	private Map<String, String> cookies;
	private Map<String, String> variables;
	private YamlResponseBody body;
	private int status;
	
	
	public YamlTest getYamlTest() {
		return yamlTest;
	}

	public void setYamlTest(YamlTest yamlTest) {
		this.yamlTest = yamlTest;
	}

	public Map<String, String> getHeaders() {
		headers = getYamlTest().getYamlTestGroup().getYamlInitGroup().replaceVariable(headers);
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public YamlResponseBody getBody() {
		return body;
	}

	public void setBody(YamlResponseBody body) {
		this.body = body;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Map<String, String> getCookies() {
		cookies = getYamlTest().getYamlTestGroup().getYamlInitGroup().replaceVariable(cookies);
		return cookies;
	}

	public void setCookies(Map<String, String> cookies) {
		this.cookies = cookies;
	}

	public Map<String, String> getVariables() {
		return variables;
	}

	public void setVariables(Map<String, String> variables) {
		this.variables = variables;
	}

}
