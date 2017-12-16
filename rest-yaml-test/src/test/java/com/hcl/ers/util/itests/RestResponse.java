package com.hcl.ers.util.itests;

import static org.hamcrest.core.IsEqual.equalTo;

import java.util.Map;

import org.json.JSONException;
import static org.hamcrest.MatcherAssert.assertThat;

import com.hcl.ers.util.itests.beans.YamlInitGroup;
import com.hcl.ers.util.itests.beans.YamlTest;
import com.hcl.ers.util.itests.util.Regex;
import com.hcl.ers.util.itests.util.Variable;
import com.jayway.restassured.response.Response;

public class RestResponse {
	
	private Response response;
	private YamlTest yamlTest;
	private YamlInitGroup yamlInitGroup;
	
	public static RestResponse build(Response response, YamlTest yamlTest, YamlInitGroup yamlInitGroup) {
		return new RestResponse(response, yamlTest, yamlInitGroup);
	}
	
	private RestResponse(Response response, YamlTest yamlTest, YamlInitGroup yamlInitGroup) {
		this.response = response;
		this.yamlTest = yamlTest;
		this.yamlInitGroup = yamlInitGroup;
	}
	
	public void doAssert() throws JSONException {
		assertThat(response.statusCode(), equalTo(yamlTest.getResponse().getStatus()));
		headersAssert();
		cookiesAssert();
		BodyAssert.build(response, yamlTest).doAssert();
		assignVariableValue();
	}

	private void assignVariableValue () {
		if(yamlTest.getResponse().getVariables() != null) {
			for(Map.Entry<String, String> entry: yamlTest.getResponse().getVariables().entrySet()) {
				if(entry.getValue().startsWith("header.")) {
					String variable = entry.getKey();
					String value = response.getHeader(entry.getValue().substring(7));
					System.out.println("variable value from header " + variable+"="+value);
					yamlInitGroup.getVariables().put(variable, value);
				} else if (entry.getValue().startsWith("cookie.")) {
					String variable = entry.getKey();
					String value = response.getCookie(entry.getValue().substring(7));
					yamlInitGroup.getVariables().put(variable, value);
					System.out.println("variable value from cookie " + variable+"="+value);
				} else if (entry.getValue().startsWith("body.")) {
					String variable = entry.getKey();
					String value = "null";
					if(entry.getValue().startsWith("body.regex")) {
						String regExPattern = entry.getValue().substring(10);
						value  = (String) Regex.find(regExPattern, response.body().asString());
					} else {
						value = response.body().jsonPath().getString(entry.getValue().substring(5));
					}
					System.out.println("variable value from body " + variable+"="+value);
					yamlInitGroup.getVariables().put(variable, value);
				}
			}
		}
	}
	
	private void headersAssert() {
		System.out.println("response headers = "+response.getHeaders());
		if (yamlTest.getResponse().getHeaders() != null) {
			for (Map.Entry<String, String> entry : yamlTest.getResponse().getHeaders().entrySet()) {
				String actual = response.getHeader(entry.getKey());
				String expected = Variable.replaceValue(entry.getValue(), yamlInitGroup);
				assertThat(actual, equalTo(expected));
			}
		}
	}
		
	private void cookiesAssert() {
		System.out.println("response cookies = "+response.getCookies());
		if (yamlTest.getResponse().getCookies() != null) {
			for (Map.Entry<String, String> entry : yamlTest.getResponse().getCookies().entrySet()) {
				String actual = response.getCookie(entry.getKey());
				String expected = Variable.replaceValue(entry.getValue(), yamlInitGroup);
				assertThat(actual, equalTo(expected));
			}
		}
	}
}
