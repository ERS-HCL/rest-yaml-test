package com.github.rest.yaml.test.beans;

import java.util.List;
import java.util.Map;

import com.github.rest.yaml.test.CurrentState;
import com.github.rest.yaml.test.util.Environment;

public class YamlTestTemplate {

	private String name;
	private String template; // template name
	
	private List<String> tags;
	private YamlRequest request;
	private YamlResponse response;
	private Map<String, String> arguments;
	
	public boolean isTagged() {
		if (Environment.instance().getTestTags() == null) {
			return true;
		}

		for (String tag : Environment.instance().getTestTags()) {
			if (getTags() != null && getTags().contains(tag)) {
				return true;
			}
		}

		return false;
	}

	public String getName() {
		if(name == null) {
			return template;
		}
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public YamlRequest getRequest() {
		if(template != null) {
			return CurrentState.getYamlTestTemplateGroup().getTemplate(template).getRequest();
		}
		return request;
	}

	public void setRequest(YamlRequest request) {
		this.request = request;
	}

	public YamlResponse getResponse() {
		if(template != null) {
			return CurrentState.getYamlTestTemplateGroup().getTemplate(template).getResponse();
		}
		return response;
	}

	public void setResponse(YamlResponse response) {
		this.response = response;
	}

	public List<String> getTags() {
		if(tags == null) {
			if(template != null) {
				return CurrentState.getYamlTestTemplateGroup().getTemplate(template).getTags();
			}
		}
		
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public Map<String, String> getArguments() {
		return arguments;
	}

	public void setArguments(Map<String, String> arguments) {
		this.arguments = arguments;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}
	
}
