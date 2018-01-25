package com.github.rest.yaml.test;

import static net.javacrumbs.jsonunit.JsonAssert.assertJsonEquals;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_ARRAY_ORDER;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_EXTRA_FIELDS;
import static net.javacrumbs.jsonunit.core.Option.TREATING_NULL_AS_ABSENT;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;
import java.util.Map;

import com.github.rest.yaml.test.beans.YamlBodyAssert;
import com.github.rest.yaml.test.beans.YamlTest;
import com.github.rest.yaml.test.expression.BodyAssertSelectExpression;
import com.github.rest.yaml.test.util.JsonMapper;
import com.github.rest.yaml.test.util.Logger;
import com.jayway.restassured.response.Response;

import net.javacrumbs.jsonunit.JsonAssert;

public class BodyAssert {
	private static Logger logger = new Logger();
	private Response response;
	private YamlTest yamlTest;
	
	public static BodyAssert build (Response response, YamlTest yamlTest) {
		return new BodyAssert(response, yamlTest);
	}
	
	private BodyAssert(Response response, YamlTest yamlTest) {
		this.response = response;
		this.yamlTest = yamlTest;
	}
	
	public void doAssert() {
		logger.debug("response body = "+response.asString());
		if(yamlTest.getResponse().getBody() != null &&  yamlTest.getResponse().getBody().getAsserts() != null) {
			for(YamlBodyAssert bodyAssert: yamlTest.getResponse().getBody().getAsserts()) {
				doAssert(bodyAssert);
			}
		}
	}
	
	private void doAssert(YamlBodyAssert bodyAssert) {
		Object value = BodyAssertSelectExpression.build(bodyAssert).eval(response);
		if (value instanceof Map) {
			jsonAssert(bodyAssert, value);
		} else if (value instanceof List) {
			jsonAssert(bodyAssert, value);
		} else {
			atomicAssert(bodyAssert, value.toString());
		}
	}
	
	private void atomicAssert(YamlBodyAssert bodyAssert, String actual) {
		String expected = bodyAssert.getValue();
		log(bodyAssert, expected, actual);
		assertThat(actual, equalTo(expected));
	}
	
	private void jsonAssert(YamlBodyAssert bodyAssert, Object map) {
		JsonAssert.setOptions(IGNORING_ARRAY_ORDER, IGNORING_EXTRA_FIELDS, TREATING_NULL_AS_ABSENT);
		String expected = bodyAssert.getValue();
		String actual = JsonMapper.toJson(map);
		log(bodyAssert, expected, actual);
		assertJsonEquals(expected, actual);
	}
	
	private void log(YamlBodyAssert bodyAssert, Object expected, Object actual) {
		String match = "equals";
		if(bodyAssert.getMatch()!=null) {
			match = bodyAssert.getMatch();
		}
		logger.info("Body assert select="+bodyAssert.getSelect()+", match="+match+", expected="+expected+" actual="+actual);
	}
	
	public static void main(String[] args) {
		JsonAssert.setOptions(IGNORING_ARRAY_ORDER, IGNORING_EXTRA_FIELDS, TREATING_NULL_AS_ABSENT);
		assertJsonEquals("{\"test\":[{\"key\":1},{\"key\":2},{\"key\":3}]}", 
			    "{\"test\":[{\"key\":3},{\"key\":2, \"extraField\":2},{\"key\":1}]}");
	}
}
