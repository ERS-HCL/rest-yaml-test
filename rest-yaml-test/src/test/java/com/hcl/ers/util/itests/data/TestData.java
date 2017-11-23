package com.hcl.ers.util.itests.data;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.jayway.restassured.path.json.JsonPath;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class TestData {
	private static ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
	private static ObjectMapper jsonMapper = new ObjectMapper();
	private JsonPath dataJsonPath;

	private Config config;
	private List<String> testFiles;
	
	public TestData(String env) {
		this.config = ConfigFactory.load("application-" + env);
		this.testFiles = Arrays.asList(config.getString("testFiles").split("[\\s,]+"));
		buildTestData();
	}
	
	private void buildTestData() {
		List<InputStream> testDataInputStreams = new ArrayList<InputStream>();
		
		for(String testFile: testFiles) {
			/** start / is required for getResourceAsStream method otherwise files is not loaded.**/
			testFile = "/"+testFile;
			testDataInputStreams.add(TestData.class.getResourceAsStream(testFile));
			/** A new line character is added otherwise two files are not mearge properly and yaml parser gives error **/
			testDataInputStreams.add(new ByteArrayInputStream("\n".getBytes()));
		}
		
		SequenceInputStream SequenceInputStream = new SequenceInputStream(Collections.enumeration(testDataInputStreams));
		//System.out.print(convertToString(SequenceInputStream));

		try {
			Map testData = yamlMapper.readValue(SequenceInputStream, Map.class);
			this.dataJsonPath = JsonPath.given(jsonMapper.writeValueAsString(testData));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	String convertToString(InputStream in){
	    String resource = new Scanner(in).useDelimiter("\\Z").next();
	    return resource;
	}
	
	public JsonPath getTestData() {
		return dataJsonPath;
	}

}
