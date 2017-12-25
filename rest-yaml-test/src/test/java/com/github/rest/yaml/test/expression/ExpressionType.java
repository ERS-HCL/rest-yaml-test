package com.github.rest.yaml.test.expression;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public enum ExpressionType {

	regex("regex"), 
	regex_match("regex.match"), // return match count
	regex_find("regex.find"), // return match values
	jsonpath("jsonpath"), 
	string("string"),
	string_equals("string.equals"),
	string_equalsIgnoreCase("string.equalsIgnoreCase"),
	string_compareTo("string.compareTo"),
	string_compareToIgnoreCase("string.compareToIgnoreCase"),
	string_contains("string.contains"),
	string_toLowerCase("string.toLowerCase"),
	string_toUpperCase("string.toUpperCase"),
	string_trim("string.trim"),
	string_substring("string.substring"),
	string_lastIndexOf("string.lastIndexOf"),
	string_indexOf("string.indexOf"),
	string_startsWith("string.startsWith"),
	string_endsWith("string.endsWith"),
	string_length("string.length"),
	string_isEmpty("string.isEmpty")	
	;
	
	private String value;
	private static List<ExpressionType> values = new LinkedList<ExpressionType>();
	
	static {
		values.addAll(Arrays.asList(ExpressionType.values()));
		values.removeAll(Arrays.asList(new ExpressionType[] {regex, string}));
	}
	
	private ExpressionType(String value) {
		this.value = value;
	}

	public static boolean exist(String name) {
		for (ExpressionType exp : values) {
			if (exp.toString().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return value;
	}
}