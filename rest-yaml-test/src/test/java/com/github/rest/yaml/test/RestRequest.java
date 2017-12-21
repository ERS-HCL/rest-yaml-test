package com.github.rest.yaml.test;

import java.util.Map;

import com.github.rest.yaml.test.beans.YamlInitGroup;
import com.github.rest.yaml.test.beans.YamlTest;
import com.github.rest.yaml.test.util.TestException;
import com.github.rest.yaml.test.util.Variable;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;

public class RestRequest {
	
	private RequestSpecification rs;
	private YamlTest yamlTest;
	private YamlInitGroup yamlInitGroup;
	
	public static RestRequest build(RequestSpecification rs, YamlTest yamlTest, YamlInitGroup yamlInitGroup) {
		return new RestRequest(rs, yamlTest, yamlInitGroup);
	}
	
	private RestRequest(RequestSpecification rs, YamlTest yamlTest, YamlInitGroup yamlInitGroup) {
		this.rs = rs;
		this.yamlTest = yamlTest;
		this.yamlInitGroup = yamlInitGroup;
	}
	
	private void addHeaders() {
		if(yamlTest.getRequest().getHeaders() != null) {
			replaceVariableValue(yamlTest.getRequest().getHeaders());
			rs.headers(yamlTest.getRequest().getHeaders());
		}
	}
	
	private void addParameters() {
		if(yamlTest.getRequest().getParameters() != null) {
			replaceVariableValue(yamlTest.getRequest().getParameters());
			rs.parameters(yamlTest.getRequest().getParameters());
		}
	}
	
	private void addCookies() {
		if(yamlTest.getRequest().getCookies() != null) {
			replaceVariableValue(yamlTest.getRequest().getCookies());
			rs.cookies(yamlTest.getRequest().getCookies());
		}
	}
	
	private void addBody() {
		if(yamlTest.getRequest().getBody() != null) {
			rs.body(yamlTest.getRequest().getBody());
		}
	}
	
	public RestResponse request() {
		if (yamlTest.getRequest().isEncodeURL() == false) {
			rs = rs.urlEncodingEnabled(false);
		}
		addHeaders();
		addParameters();
		addCookies();
		addBody();
		
		final Response response;
		String uri = Variable.replaceValue(yamlTest.getRequest().getUri(), yamlInitGroup);
		
		if(yamlTest.getRequest().getMethod().equalsIgnoreCase("get")) {
			response = rs.get(uri);
		} else if (yamlTest.getRequest().getMethod().equalsIgnoreCase("post")) {
			response = rs.post(uri);
			
		} else if (yamlTest.getRequest().getMethod().equalsIgnoreCase("put")) {
			response = rs.put(uri);
		} else if (yamlTest.getRequest().getMethod().equalsIgnoreCase("delete")) {
			response = rs.delete(uri);
		} else {
			throw new TestException("Request method is not get, post, put and delete for uri: "+yamlTest.getRequest().getUri());
		}
		
		return RestResponse.build(response, yamlTest, yamlInitGroup);
	}
	
	private void replaceVariableValue(Map<String, String> map) {
		for(Map.Entry<String, String> entry: map.entrySet()) {
			map.put(entry.getKey(), Variable.replaceValue(entry.getValue(), yamlInitGroup));
		}
	}

}
