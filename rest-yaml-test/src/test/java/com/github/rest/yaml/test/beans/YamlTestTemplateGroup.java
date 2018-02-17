package com.github.rest.yaml.test.beans;

import java.util.List;

public class YamlTestTemplateGroup {
	private List<YamlTestTemplate> templates;

	public List<YamlTestTemplate> getTemplates() {
		return templates;
	}

	public void setTemplates(List<YamlTestTemplate> templates) {
		this.templates = templates;
	}
	
	public YamlTestTemplate getTemplate(String name) {
		for(YamlTestTemplate template: templates) {
			if(name.equals(template.getName())) {
				return template;
			}
		}
		
		return null;
	}
}
