package com.github.rest.yaml.test.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.rest.yaml.test.beans.YamlInitGroup;

public class Variable {
	private static Logger logger = new Logger();
	
	public static String replaceValue(String input, YamlInitGroup yamlInitGroup) {
		Pattern p = Pattern.compile("\\$\\{[a-zA-Z][a-zA-Z0-9_\\-]*\\}");
		Matcher m = p.matcher(input);
		
		String output = input;
		while(m.find()) {
			String matched = m.group();
			String variable = matched.substring(2, matched.length()-1);
			String value = yamlInitGroup.getVariables().get(variable);
			if(value == null ) {
				value="null";
			}
			output = m.replaceFirst(value);
			m = p.matcher(output);
		}
		
		if(!input.equals(output)) {
			logger.debug("string "+input+" replaced by "+output);
		}
		
		return output;
	}
}
