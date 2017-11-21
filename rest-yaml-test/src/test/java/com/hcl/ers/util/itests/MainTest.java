package com.hcl.ers.util.itests;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.skyscreamer.jsonassert.JSONAssert;

import com.hcl.ers.util.itests.beans.InitGroup;
import com.hcl.ers.util.itests.beans.JsonAssert;
import com.hcl.ers.util.itests.beans.RestTest;
import com.hcl.ers.util.itests.beans.TestGroup;
import com.hcl.ers.util.itests.util.JsonMapper;
import com.hcl.ers.util.itests.util.TestException;
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

	@Before
	public void setUp() {
		super.setUp();
		this.initGroup = getInitGroupData();
	}

	@Test
	public void testWithRestAssured() throws Exception {
		if(testGroup.isSkip()) {
			System.out.println("->skipped test group "+testGroup.getName());
			return;
		}
		
		for (RestTest test : testGroup.getTests()) {
			
			if(test.isSkip()) {
				System.out.println("-->skipped test "+testGroup.getName()+"->"+test.getName());
			}
			
			System.out.println("-->start test "+testGroup.getName()+"->"+test.getName());
			
			RequestSpecification rs = given().spec(rspec);
		
			addHeaders(rs, test);
			addParameters(rs, test);
			addCookies(rs, test);
			addBody(rs, test);
			
			Response response = request(rs, test);
			
			Assert.assertThat(response.statusCode(), equalTo(test.getResponse().getStatus()));
			headersAssert(response, test);
			cookiesAssert(response, test);
			bodyAssert(response, test);
			assignVariableValue(response, test);
			
			System.out.println("-->end test "+testGroup.getName()+"->"+test.getName());
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
	
	private void addBody(RequestSpecification rs, RestTest test) {
		if(test.getRequest().getBody() != null) {
			rs.body(test.getRequest().getBody());
		}
	}
	
	private Response request(RequestSpecification rs, RestTest test) {
		final Response response;
		String uri = replaceVariableValue(test.getRequest().getUri());
		
		if(test.getRequest().getMethod().equalsIgnoreCase("get")) {
			response = rs.get(uri);
		} else if (test.getRequest().getMethod().equalsIgnoreCase("post")) {
			response = rs.post(uri);
			
		} else if (test.getRequest().getMethod().equalsIgnoreCase("put")) {
			response = rs.put(uri);
		} else if (test.getRequest().getMethod().equalsIgnoreCase("delete")) {
			response = rs.delete(uri);
		} else {
			throw new TestException("Request method is not get, post, put and delete for testGroup="+ 
									testGroup.getName()+
									" and uri="+test.getRequest().getUri());
		}
		
		return response;
	}
	
	private void replaceVariableValue(Map<String, String> map) {
		for(Map.Entry<String, String> entry: map.entrySet()) {
			map.put(entry.getKey(), replaceVariableValue(entry.getValue()));
		}
	}
	
	private String replaceVariableValue(String input) {
		Pattern p = Pattern.compile("\\$\\{[a-zA-Z][a-zA-Z0-9_\\-]*\\}");
		Matcher m = p.matcher(input);
		
		String output = input;
		while(m.find()) {
			String matched = m.group();
			String variable = matched.substring(2, matched.length()-1);
			String value = initGroup.getVariables().get(variable);
			if(value == null ) {
				value="null";
			}
			output = m.replaceFirst(value);
			m = p.matcher(output);
		}
		
		if(!input.equals(output)) {
			System.out.println("string "+input+" replaced by "+output);
		}
		
		return output;
	}
	
	private void assignVariableValue (Response response, RestTest test) {
		if(test.getResponse().getVariables() != null) {
			for(Map.Entry<String, String> entry: test.getResponse().getVariables().entrySet()) {
				if(entry.getValue().startsWith("header.")) {
					String variable = entry.getKey();
					String value = response.getHeader(entry.getValue().substring(7));
					System.out.println("variable value from header " + variable+"="+value);
					initGroup.getVariables().put(variable, value);
				} else if (entry.getValue().startsWith("cookie.")) {
					String variable = entry.getKey();
					String value = response.getCookie(entry.getValue().substring(7));
					initGroup.getVariables().put(variable, value);
					System.out.println("variable value from cookie " + variable+"="+value);
				} else if (entry.getValue().startsWith("body.")) {
					String variable = entry.getKey();
					String value = response.body().jsonPath().getString(entry.getValue().substring(5));
					System.out.println("variable value from body " + variable+"="+value);
					initGroup.getVariables().put(variable, value);
				}
			}
		}
	}
	
	private void headersAssert(Response response, RestTest test) {
		System.out.println("response headers = "+response.getHeaders());
		if (test.getResponse().getHeaders() != null) {
			for (Map.Entry<String, String> entry : test.getResponse().getHeaders().entrySet()) {
				String actual = response.getHeader(entry.getKey());
				String expected = replaceVariableValue(entry.getValue());
				Assert.assertThat(actual, equalTo(expected));
			}
		}
	}
		
	private void cookiesAssert(Response response, RestTest test) {
		System.out.println("response cookies = "+response.getCookies());
		if (test.getResponse().getCookies() != null) {
			for (Map.Entry<String, String> entry : test.getResponse().getCookies().entrySet()) {
				String actual = response.getCookie(entry.getKey());
				String expected = replaceVariableValue(entry.getValue());
				Assert.assertThat(actual, equalTo(expected));
			}
		}
	}
	
	private void bodyAssert(Response response, RestTest test) throws JSONException {
		System.out.println("response body = "+response.asString());
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
