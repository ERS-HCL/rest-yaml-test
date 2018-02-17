package com.github.rest.yaml.test.beans;

import java.util.Map;

import com.github.rest.yaml.test.util.JsonMapper;
import com.github.rest.yaml.test.util.Logger;
import com.github.rest.yaml.test.util.Regex;
import com.jayway.jsonpath.JsonPath;
import com.jayway.restassured.response.Response;

public class YamlInitGroup {
	
	private static Logger logger = new Logger();
	
	Map<String, String> variables;
	
	public Map<String, String> getVariables() {
		return variables;
	}

	public void setVariables(Map<String, String> variables) {
		this.variables = variables;
	}
	
	public void storeVariableValue(YamlTest yamlTest, Response response) {
		if (yamlTest.getResponse().getVariables() != null) {
			for (Map.Entry<String, String> entry : yamlTest.getResponse().getVariables().entrySet()) {
				if (entry.getValue().startsWith("header.")) {
					String variable = entry.getKey();
					String value = response.getHeader(entry.getValue().substring(7));
					logger.debug("variable value from header " + variable + "=" + value);
					getVariables().put(variable, value);
				} else if (entry.getValue().startsWith("cookie.")) {
					String variable = entry.getKey();
					String value = response.getCookie(entry.getValue().substring(7));
					getVariables().put(variable, value);
					logger.debug("variable value from cookie " + variable + "=" + value);
				} else if (entry.getValue().startsWith("body.")) {
					String variable = entry.getKey();
					String value = "null";
					if (entry.getValue().startsWith("body.regex")) {
						String regExPattern = entry.getValue().substring(10);
						value = (String) Regex.find(regExPattern, response.body().asString());
					} else {
						value = JsonMapper.toJson(JsonPath.parse(response.body().asString()).read(entry.getValue().substring(5)));				
					}
					logger.debug("variable value from body " + variable + "=" + value);
					getVariables().put(variable, value);
				}
			}
		}
	}

}
