package com.github.rest.yaml.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;

import com.github.rest.yaml.test.beans.YamlBodyAssert;
import com.github.rest.yaml.test.beans.YamlTest;
import com.github.rest.yaml.test.selector.BodyAssertSelector;
import com.github.rest.yaml.test.util.JsonMapper;
import com.github.rest.yaml.test.util.Logger;
import com.github.rest.yaml.test.util.TestException;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;

public class BodyAssert {
	private static Logger logger = new Logger();
	private Response response;
	private YamlTest yamlTest;
	private BodyAssertSelector BodyAssertSelector = new BodyAssertSelector();
	
	public static BodyAssert build (Response response, YamlTest yamlTest) {
		return new BodyAssert(response, yamlTest);
	}
	
	private BodyAssert(Response response, YamlTest yamlTest) {
		this.response = response;
		this.yamlTest = yamlTest;
	}
	
	public void doAssert() throws JSONException {
		logger.debug("response body = "+response.asString());
		if(yamlTest.getResponse().getBody() != null &&  yamlTest.getResponse().getBody().getAsserts() != null) {
			for(YamlBodyAssert bodyAssert: yamlTest.getResponse().getBody().getAsserts()) {
				doAssert(bodyAssert);
			}
		}
	}
	
	private void doAssert(YamlBodyAssert bodyAssert) throws JSONException {
		Object value = BodyAssertSelector.select(response, bodyAssert);
		if (value instanceof Map) {
			jsonAssert(bodyAssert, (Map) value);
		} else if (value instanceof List) {
			List<Object> v = ((List<Object>) value);
			if (v.isEmpty()) {
				throw new TestException("Empty list test=" + yamlTest.getName() + " select=" + bodyAssert.getSelect());
			}
			if (v.get(0) instanceof Map) {
				jsonAssert(bodyAssert, (Map) v);
			} else {
				atomicCollectionAssert(bodyAssert, v);
			}

		} else {
			atomicAssert(bodyAssert, value.toString());
		}
	}
	
	private void atomicCollectionAssert(YamlBodyAssert bodyAssert, List<Object> actual) {
		List<Object> expected = JsonPath.from(bodyAssert.getValue()).get();		
		
		if(bodyAssert.getMatch() != null && bodyAssert.getMatch().equalsIgnoreCase("hasItems")) {
			log(bodyAssert, expected, actual);
			assertThat(actual, hasItems(expected.toArray()));
		} else {
			log(bodyAssert, expected, actual);
			assertThat(actual, equalTo(expected));
		}
	}
	
	private void atomicAssert(YamlBodyAssert bodyAssert, String actual) {
		String expected = bodyAssert.getValue();
		log(bodyAssert, expected, actual);
		assertThat(actual, equalTo(expected));
	}
	
	private void jsonAssert(YamlBodyAssert bodyAssert, Map map) throws JSONException {
		String expected = bodyAssert.getValue();
		String actual = JsonMapper.toJson(map);
		log(bodyAssert, expected, actual);
		
		if(bodyAssert.getMatch() != null && bodyAssert.getMatch().equalsIgnoreCase("strict")) {
			JSONAssert.assertEquals(expected, actual, true);
		} else {
			JSONAssert.assertEquals(expected, actual, false);
		}
	}
	
	private void log(YamlBodyAssert bodyAssert, Object expected, Object actual) {
		String match = "equals";
		if(bodyAssert.getMatch()!=null) {
			match = bodyAssert.getMatch();
		}
		logger.info("Body assert select="+bodyAssert.getSelect()+" match="+match+", expected="+expected+" actual="+actual);
	}
	
}
