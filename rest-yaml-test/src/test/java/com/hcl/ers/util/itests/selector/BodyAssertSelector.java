package com.hcl.ers.util.itests.selector;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.hcl.ers.util.itests.beans.YamlBodyAssert;
import com.hcl.ers.util.itests.util.Regex;
import com.jayway.restassured.response.Response;

public class BodyAssertSelector {
	
	public Object select(Response response, YamlBodyAssert bodyAssert) {
		Object value = null;
		if(bodyAssert.getJsonPath() != null) {
			value = response.body().jsonPath().get(bodyAssert.getJsonPath());
		} else if (bodyAssert.getRegex() != null) {
			value = Regex.find(bodyAssert.getRegex(), response.body().asString());
		}
		return value;
	}
	
	private List<?> operations(String select) {
		StringTokenizer tok = new StringTokenizer(select, "|");
		List<String> operations = new ArrayList<>();
		while (tok.hasMoreTokens()) {
	         operations.add(tok.nextToken());
	     }

		return null;
	}
	
	
}
