package com.hcl.ers.util.itests.data;

import java.io.InputStream;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.jayway.restassured.path.json.JsonPath;

public class TestData {
	private static ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
	private static ObjectMapper jsonMapper = new ObjectMapper();
	private JsonPath dataJsonPath;

	private String env = "dev";

	public TestData(String env) {
		this.env = env;
		InputStream dataInputStream = TestData.class.getResourceAsStream("/test-data-" + env + ".yaml");

		try {
			Map testData = yamlMapper.readValue(dataInputStream, Map.class);
			this.dataJsonPath = JsonPath.given(jsonMapper.writeValueAsString(testData));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public JsonPath getTestData() {
		return dataJsonPath;
	}

}
