package com.github.rest.yaml.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import java.util.Map;

import org.json.JSONException;

import com.github.rest.yaml.test.beans.YamlInitGroup;
import com.github.rest.yaml.test.beans.YamlTest;
import com.github.rest.yaml.test.util.Logger;
import com.jayway.restassured.response.Response;

public class RestResponse {
	private static Logger logger = new Logger();
	private Response response;
	private YamlTest yamlTest;
	private YamlInitGroup yamlInitGroup;

	public static RestResponse build(Response response, YamlTest yamlTest) {
		return new RestResponse(response, yamlTest, yamlTest.getYamlTestGroup().getYamlInitGroup());
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
		yamlInitGroup.storeVariableValue(yamlTest, response);
	}

	private void headersAssert() {
		logger.debug("response headers = " + response.getHeaders());
		if (yamlTest.getResponse().getHeaders() != null) {
			for (Map.Entry<String, String> entry : yamlTest.getResponse().getHeaders().entrySet()) {
				String actual = response.getHeader(entry.getKey());
				String expected = entry.getValue();
				logger.info("Header assert=" + entry.getKey() + ", expected=" + expected + " actual=" + actual);
				assertThat(actual, equalTo(expected));
			}
		}
	}

	private void cookiesAssert() {
		logger.debug("response cookies = " + response.getCookies());
		if (yamlTest.getResponse().getCookies() != null) {
			for (Map.Entry<String, String> entry : yamlTest.getResponse().getCookies().entrySet()) {
				String actual = response.getCookie(entry.getKey());
				String expected = entry.getValue();
				logger.info("Cookie assert=" + entry.getKey() + ", expected=" + expected + " actual=" + actual);
				assertThat(actual, equalTo(expected));
			}
		}
	}
}
