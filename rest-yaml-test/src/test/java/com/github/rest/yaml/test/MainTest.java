package com.github.rest.yaml.test;

import static com.jayway.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.Executable;

import com.github.rest.yaml.test.beans.YamlDataGroup;
import com.github.rest.yaml.test.beans.YamlInitGroup;
import com.github.rest.yaml.test.beans.YamlTest;
import com.github.rest.yaml.test.beans.YamlTestGroup;
import com.github.rest.yaml.test.certificate.CertificateLoader;
import com.github.rest.yaml.test.util.Environment;
import com.jayway.restassured.specification.RequestSpecification;

public class MainTest extends AbstractITest {

	public static int testGroupCount = 0;
	private static List<YamlTestGroup> yamlTestGroups;
	private static YamlInitGroup yamlInitGroup;
	private static YamlDataGroup yamlDataGroup;
	
	@BeforeAll
	public static void setUp() throws Exception {
		abstractSetUp();
		yamlInitGroup = getInitGroup();
		CertificateLoader.instance().loadCertificates(certificates);
		logger.info("Certificates loading done.");
		logger.info("baseURL="+baseURL+" port="+port);
		logger.info("groupTags="+Environment.instance().getGroupTags()+", testTags="+Environment.instance().getTestTags());
		yamlTestGroups = getTestGroups();
		yamlDataGroup = getDataGroup();
	}

	@TestFactory
	public Stream<DynamicTest> tests() throws Exception {

		Collection<DynamicTest> dynamicTests = new ArrayList<DynamicTest>();

		for (YamlTestGroup yamlTestGroup : yamlTestGroups) {
			if (!yamlTestGroup.isTagged()) {
				logger.info("->testGroup skipped tag does not exist for testGroup name=" 
			                + yamlTestGroup.getName()+", tags="+yamlTestGroup.getTags());
				continue;
			}

			for (YamlTest yamlTest : yamlTestGroup.getTests()) {
				final String testcaseName = "testGroup=" + yamlTestGroup.getName()
				                            + ", test=" + yamlTest.getName()
				                            + ", group tags="+yamlTestGroup.getTags()
				                            + ", test tags="+yamlTest.getTags();
				if (!yamlTest.isTagged()) {
					logger.info("->test skipped tag does not exist " + testcaseName);
					continue;
				}
				
				
				Executable executable = setupTest(yamlTestGroup, yamlTest, testcaseName);
				DynamicTest dTest = DynamicTest.dynamicTest(testcaseName, executable);
				dynamicTests.add(dTest);

			}
		}

		return dynamicTests.stream();
	}

	private Executable setupTest(YamlTestGroup yamlTestGroup, YamlTest yamlTest, String testcaseName) {

		RequestSpecification rs = given().spec(rspec);

		Executable executable = () -> {
			logger.log("\n\n\n-->start " + testcaseName);
			// Initialize current state to be access globally
			CurrentState.setState(yamlInitGroup, yamlTestGroup, yamlDataGroup, yamlTest);

			try {
				RestRequest.build(rs, yamlTest).request().doAssert();
			} catch (Throwable e) {
				e.printStackTrace();
				throw e;
			}

		};

		return executable;
	}
	
}
