package com.github.rest.yaml.test;

import static com.jayway.restassured.config.EncoderConfig.encoderConfig;
import static com.jayway.restassured.config.RedirectConfig.redirectConfig;

import java.util.List;
import java.util.Map;

import com.github.rest.yaml.test.beans.YamlInitGroup;
import com.github.rest.yaml.test.beans.YamlTestGroup;
import com.github.rest.yaml.test.data.TestData;
import com.github.rest.yaml.test.util.Environment;
import com.github.rest.yaml.test.util.JsonMapper;
import com.github.rest.yaml.test.util.Logger;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.config.RestAssuredConfig;
import com.jayway.restassured.config.SSLConfig;
import com.jayway.restassured.specification.RequestSpecification;


public abstract class AbstractITest {
	
	public static int port;
	public static String baseURL;
	public static RequestSpecification rspec;
	public static TestData testData = new TestData(Environment.instance());
	static Logger logger = new Logger();
	
	public static void abstractSetUp() throws Exception {
		baseURL = Environment.instance().getBaseURL();
		port = Environment.instance().getPort();

		final RequestSpecBuilder build = new RequestSpecBuilder().setBaseUri(baseURL).setPort(port);

		rspec = build.build();
		RestAssured.config().sslConfig(SSLConfig.sslConfig().relaxedHTTPSValidation());
		RestAssured.config = new RestAssuredConfig()
								.encoderConfig(encoderConfig().defaultContentCharset("UTF-8"))
								.redirect(redirectConfig().followRedirects(false));
	}
	
	public static List<YamlTestGroup> getTestGroupData() {
		long startTime = System.currentTimeMillis();
		List<YamlTestGroup> groups = JsonMapper.toObject(testData.getTestData().getObject("testGroup", List.class), YamlTestGroup.class);
		long endTime = System.currentTimeMillis();
		
		logger.info("total testGroup count="+groups.size()+" yaml parsing time in millis="+(endTime-startTime));
		return groups;
	}
	
	public static YamlInitGroup getInitGroupData() {
		YamlInitGroup initGroup = JsonMapper.toObject(testData.getTestData().getObject("initGroup", Map.class), YamlInitGroup.class);
		return initGroup;
	}
}
