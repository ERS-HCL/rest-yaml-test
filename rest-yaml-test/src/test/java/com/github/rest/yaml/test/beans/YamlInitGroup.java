package com.github.rest.yaml.test.beans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.rest.yaml.test.CurrentState;
import com.github.rest.yaml.test.util.Logger;
import com.github.rest.yaml.test.util.Regex;
import com.jayway.restassured.response.Response;

public class YamlInitGroup {
	
	private static Logger logger = new Logger();
	
	Map<String, String> variables;
	List<String> certificates;
	
	public Map<String, String> getVariables() {
		return variables;
	}

	public void setVariables(Map<String, String> variables) {
		this.variables = variables;
	}
	
	public List<String> getCertificates() {
		return certificates;
	}

	public void setCertificates(List<String> certificates) {
		this.certificates = certificates;
	}
	
	public String replaceVariable(String input) {
		if (input == null) {
			return null;
		}

		//Pattern p = Pattern.compile("\\$\\{[a-zA-Z][a-zA-Z0-9_\\-]*\\}");
		//ignore json-unit tags.
		Pattern p = Pattern.compile("\\$\\{(?!json\\-unit\\.)[a-zA-Z][a-zA-Z0-9_\\-]*\\}");
		Matcher m = p.matcher(input);

		String output = input;
		while (m.find()) {
			String matched = m.group();
			String variable = matched.substring(2, matched.length() - 1);
			/*
			 * first try with variables then constants. This means constants takes precedence.
			 */
			String value = getVariables().get(variable);
			if(CurrentState.getYamlDataGroup().getConstants().get(variable) != null) {
				value = CurrentState.getYamlDataGroup().getConstants().get(variable);
			}
			if (value == null) {
				value = "null";
				logger.info(" Warning variable/constant="
				             + variable + " does not exist for input="
						     +input+". variable/constant replaced with 'null' string");
			}
			
			output = m.replaceFirst(value);
			m = p.matcher(output);
		}

		if (!input.equals(output)) {
			logger.debug("string " + input + " replaced by " + output);
		}

		return output;
	}

	public Map<String, String> replaceVariable(Map<String, String> map) {
		if (map == null) {
			return null;
		}

		Map<String, String> output = new HashMap<String, String>();

		for (Map.Entry<String, String> entry : map.entrySet()) {
			output.put(entry.getKey(), replaceVariable(entry.getValue()));
		}

		return output;
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
						value = response.body().jsonPath().getString(entry.getValue().substring(5));
					}
					logger.debug("variable value from body " + variable + "=" + value);
					getVariables().put(variable, value);
				}
			}
		}
	}

}
