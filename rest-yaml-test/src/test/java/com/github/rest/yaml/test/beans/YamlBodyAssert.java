package com.github.rest.yaml.test.beans;

import com.github.rest.yaml.test.CurrentState;
import com.github.rest.yaml.test.expression.ExpressionType;

public class YamlBodyAssert {
	
	private String jsonPath;
	private String regex;
	private String select;
	private String match;
	private String value;

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
		value = CurrentState.getYamlInitGroup().replaceVariable(value);
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getSelect() {
		if (select == null) {
			if (regex != null) {
				select = ExpressionType.regex_find + " " + regex;
			}
			if (jsonPath != null) {
				select = ExpressionType.jsonpath + " " + jsonPath;
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
