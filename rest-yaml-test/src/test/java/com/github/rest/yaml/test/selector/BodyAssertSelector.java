package com.github.rest.yaml.test.selector;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.github.rest.yaml.test.beans.YamlBodyAssert;
import com.github.rest.yaml.test.util.JsonMapper;
import com.github.rest.yaml.test.util.Regex;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;

public class BodyAssertSelector {
	
	public Object select(Response response, YamlBodyAssert bodyAssert) {
		Object value = null;
		String data = response.body().asString();
				
		if (bodyAssert.getSelect() != null) {
			List<String> ops = operations(bodyAssert.getSelect());
			for(String op: ops) {
				if (op.startsWith(YamlBodyAssert.jsonpathPrefix)) {
					//remove "jsonpath." prefix
					op = op.substring(9);
					value = JsonPath.from(data).get(op);
				} else if (op.startsWith(YamlBodyAssert.regexPrefix)) {
					//remove "regex." prefix
					op = op.substring(6);
					value = Regex.find(op, data);
				}
				
				data = JsonMapper.toJson(value);
			}
		}
		return value;
	}
	
	private List<String> operations(String select) {
		StringTokenizer tok = new StringTokenizer(select, "|");
		List<String> operations = new ArrayList<>();
		while (tok.hasMoreTokens()) {
	         operations.add(tok.nextToken().trim());
	     }
		return operations;
	}
	
	
}
