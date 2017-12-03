package com.hcl.ers.util.itests.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hcl.ers.util.itests.beans.YamlInitGroup;

public class Variable {
	
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
			System.out.println("string "+input+" replaced by "+output);
		}
		
		return output;
	}
}
