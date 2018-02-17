package com.github.rest.yaml.test.beans;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.rest.yaml.test.CurrentState;
import com.github.rest.yaml.test.util.Logger;

public class YamlTest extends YamlTestTemplate {
	private static Logger logger = new Logger();
	
	public String replaceVariable(String input) {
		if (input == null) {
			return null;
		}

		// Pattern p = Pattern.compile("\\$\\{[a-zA-Z][a-zA-Z0-9_\\-]*\\}");
		// ignore json-unit tags.
		Pattern p = Pattern.compile("\\$\\{(?!json\\-unit\\.)[a-zA-Z][a-zA-Z0-9_\\-]*\\}");
		Matcher m = p.matcher(input);

		String output = input;
		while (m.find()) {
			String matched = m.group();
			String variable = matched.substring(2, matched.length() - 1);
			/*
			 * first try with global variables then constants then template arguments. This means template arguments takes
			 * precedence.
			 */
			String value = null;
			
			if (CurrentState.getYamlInitGroup().getVariables().get(variable) != null) {
				value = CurrentState.getYamlInitGroup().getVariables().get(variable);
			} else if (CurrentState.getYamlDataGroup().getConstants().get(variable) != null) {
				value = CurrentState.getYamlDataGroup().getConstants().get(variable);
			} else if (getArguments().get(variable) != null) {
				value = getArguments().get(variable);
			}
			
			if (value == null) {
				value = "null";
				logger.info(" Warning variable/constant=" + variable + " does not exist for input=" + input
						+ ". variable/constant replaced with 'null' string");
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
}
