package com.hcl.ers.util.itests;

import static com.jayway.restassured.config.EncoderConfig.encoderConfig;
import static com.jayway.restassured.config.RedirectConfig.redirectConfig;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.hcl.ers.util.itests.beans.InitGroup;
import com.hcl.ers.util.itests.beans.TestGroup;
import com.hcl.ers.util.itests.data.TestData;
import com.hcl.ers.util.itests.util.JsonMapper;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.config.RestAssuredConfig;
import com.jayway.restassured.specification.RequestSpecification;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;


@RunWith(value = Parameterized.class)
public abstract class AbstractITest {
	
	
	
	public static final String ENV = "env";

	public static final String[] ENV_NAMES = { "dev", "ci", "test", "final" };

	public Config conf;

	public int timeout;

	public int port;

	public String baseURL;

	public RequestSpecification rspec;
	
	public static TestData testData = new TestData(getEnv());
	
	
	/**
	 * Initial Setup.
	 */
	@Before
	public void setUp() {
		this.conf = ConfigFactory.load("application-" + getEnv());
		this.baseURL = conf.getString("server.baseURI");
		this.port = conf.getInt("server.port");
		this.timeout = conf.getInt("idp.api.timeout");

		final RequestSpecBuilder build = new RequestSpecBuilder().setBaseUri(baseURL).setPort(port);

		rspec = build.build();
		RestAssured.config = new RestAssuredConfig()
								.encoderConfig(encoderConfig().defaultContentCharset("UTF-8"))
								.redirect(redirectConfig().followRedirects(true).and().maxRedirects(1));
		RestAssured.useRelaxedHTTPSValidation();
	}
	
	public static List<TestGroup> getTestGroupData() {
		List<TestGroup> groups = JsonMapper.toObject(testData.getTestData().getObject("testGroup", List.class), TestGroup.class);
		return groups;
	}
	
	public static InitGroup getInitGroupData() {
		InitGroup initGroup = JsonMapper.toObject(testData.getTestData().getObject("initGroup", Map.class), InitGroup.class);
		return initGroup;
	}
	
	private static String getEnv() {
		final String env = System.getProperty(AbstractITest.ENV);
		if (env != null && Arrays.asList(AbstractITest.ENV_NAMES).contains(env)) {
			return env;
		} else {
			return "dev";
		}
	}
}
