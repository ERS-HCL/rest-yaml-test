package com.hcl.ers.util.itests.util;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.skyscreamer.jsonassert.JSONAssert;

import com.hcl.ers.util.itests.AbstractITest;
import com.hcl.ers.util.itests.beans.InitGroup;
import com.hcl.ers.util.itests.beans.JsonAssert;
import com.hcl.ers.util.itests.beans.RestTest;
import com.hcl.ers.util.itests.beans.TestGroup;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;

public class MainTest extends AbstractITest {

	@Parameter(value = 0)
	public TestGroup testGroup;
    public InitGroup initGroup;
    
	@Parameters(name = "{index}: test - {0} ")
	public static List<TestGroup> data() throws Exception {
		return getTestGroupData();
	}

	/**
	 * Setup.
	 */
	@Before
	public void setUp() {
		super.setUp();
		this.initGroup = getInitGroupData();
	}

	@Test
	public void testWithRestAssured() throws Exception {
		for (RestTest test : testGroup.getTests()) {
			
			RequestSpecification rs = given().spec(rspec);
		
			addHeaders(rs, test);
			addParameters(rs, test);
			addCookies(rs, test);
			addBody(rs, test);
			
			Response response = request(rs, test);
			
			Assert.assertThat(response.statusCode(), equalTo(test.getResponse().getStatus()));
			bodyAssert(response, test);
			headersAssert(response, test);
			cookiesAssert(response, test);
		}
	}
	
	
	private void addHeaders(RequestSpecification rs, RestTest test) {
		if(test.getRequest().getHeaders() != null) {
			replaceVariableValue(test.getRequest().getHeaders());
			rs.headers(test.getRequest().getHeaders());
		}
	}
	
	private void addParameters(RequestSpecification rs, RestTest test) {
		if(test.getRequest().getParameters() != null) {
			replaceVariableValue(test.getRequest().getParameters());
			rs.parameters(test.getRequest().getParameters());
		}
	}
	
	private void addCookies(RequestSpecification rs, RestTest test) {
		if(test.getRequest().getCookies() != null) {
			replaceVariableValue(test.getRequest().getCookies());
			rs.cookies(test.getRequest().getCookies());
		}
	}
	
	private void replaceVariableValue(Map<String, String> map) {
		for(Map.Entry<String, String> entry: map.entrySet()) {
			if(entry.getValue().startsWith("$")) {
				String variable = entry.getValue().substring(1);
				String value = initGroup.getVariables().get(variable);
				map.put(entry.getKey(), value);
			}
		}
	}
	
	
	private void addBody(RequestSpecification rs, RestTest test) {
		if(test.getRequest().getBody() != null) {
			rs.body(test.getRequest().getBody());
		}
	}
	
	private Response request(RequestSpecification rs, RestTest test) {
		
		final Response response;
		
		if(test.getRequest().getMethod().equalsIgnoreCase("get")) {
			response = rs.get(test.getRequest().getUri());
		} else if (test.getRequest().getMethod().equalsIgnoreCase("post")) {
			response = rs.post(test.getRequest().getUri());
			
		} else if (test.getRequest().getMethod().equalsIgnoreCase("put")) {
			response = rs.put(test.getRequest().getUri());
		} else if (test.getRequest().getMethod().equalsIgnoreCase("delete")) {
			response = rs.delete(test.getRequest().getUri());
		} else {
			throw new TestException("Request method is not get, post, put and delete for testGroup="+ 
									testGroup.getName()+
									" and uri="+test.getRequest().getUri());
		}
		
		return response;
	}
	
	private void headersAssert(Response response, RestTest test) {
		if(test.getResponse().getHeaders() != null) {
			for(Map.Entry<String, String> entry: test.getResponse().getHeaders().entrySet()) {
				if(entry.getKey().startsWith("$")) {
					String variable = entry.getKey().substring(1);
					String value = response.getHeader(variable);
					initGroup.getVariables().put(variable, value);
				} else {
					String actual = response.getHeader(entry.getKey());
					String expected = entry.getValue();
					Assert.assertThat(actual, equalTo(expected));
				}
			}
		}
	}
		
	private void cookiesAssert(Response response, RestTest test) {
		if(test.getResponse().getCookies() != null) {
			for(Map.Entry<String, String> entry: test.getResponse().getCookies().entrySet()) {
				if(entry.getKey().startsWith("$")) {
					String variable = entry.getKey().substring(1);
					String value = response.getCookie(variable);
					initGroup.getVariables().put(variable, value);
				} else {
					String actual = response.getCookie(entry.getKey());
					String expected = entry.getValue();
					Assert.assertThat(actual, equalTo(expected));
				}
			}
		}
	}
	
	private void bodyAssert(Response response, RestTest test) throws JSONException {
		if(test.getResponse().getBody() != null &&  test.getResponse().getBody().getAsserts() != null) {
			for(JsonAssert jsonAssert: test.getResponse().getBody().getAsserts()) {
				Object value = response.body().jsonPath().get(jsonAssert.getJsonPath());
				if(value instanceof Map) {
					bodyJsonAssert(jsonAssert, response);
				} else if (value instanceof List) {
					if(((List)value).get(0) instanceof Map) {
						bodyJsonAssert(jsonAssert, response);
					} else {
						bodyCollectionValueAssert(response, jsonAssert);
					}
					
				} else {
					bodyAtomicValueAssert(response, jsonAssert);
				}
			}
		}
	}
	
	private void bodyCollectionValueAssert(Response response, JsonAssert jsonAssert) {
		List<?> expected = Arrays.asList(jsonAssert.getValue().split("[\\s,]+"));
		List<?> actual = response.body().jsonPath().getList(jsonAssert.getJsonPath());
		Assert.assertThat(actual, equalTo(expected));
	}
	
	private void bodyAtomicValueAssert(Response response, JsonAssert jsonAssert) {
		String expected = jsonAssert.getValue();
		String actual = response.body().jsonPath().getString(jsonAssert.getJsonPath());
		Assert.assertThat(actual, equalTo(expected));
	}
	
	private void bodyJsonAssert(JsonAssert jsonAssert, Response response) throws JSONException {
		String expected = jsonAssert.getValue();
		String actual = JsonMapper.toJson(response.body().jsonPath().get(jsonAssert.getJsonPath()));
		
		if(jsonAssert.getMatch() != null && jsonAssert.getMatch().equalsIgnoreCase("strict")) {
			JSONAssert.assertEquals(expected, actual, true);
		} else {
			JSONAssert.assertEquals(expected, actual, false);
		}
	}
	
}
