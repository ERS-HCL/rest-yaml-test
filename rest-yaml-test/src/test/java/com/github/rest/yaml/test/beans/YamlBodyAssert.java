package com.github.rest.yaml.test.beans;

public class YamlBodyAssert {

	String jsonPath;
	String regex;
	String select;
	String match;
	String value;
	
	public static enum ExpressionType {
		regex,
		jsonpath,
		string
	}
	
	public String getJsonPath() {
		return jsonPath;
	}

	public void setJsonPath(String jsonPath) {
		this.jsonPath = jsonPath;
	}

	public String getMatch() {
		return match;
	}

	public void setMatch(String match) {
		this.match = match;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getSelect() {
		if(select == null) {
			if(regex != null) {
				select = ExpressionType.regex + " " + regex;
			}
			if(jsonPath != null) {
				select = ExpressionType.jsonpath +" " + jsonPath;
			}
		}
		return select;
	}

	public void setSelect(String select) {
		this.select = select;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}
}
