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

import com.github.rest.yaml.test.beans.YamlTest;
import com.github.rest.yaml.test.beans.YamlTestGroup;
import com.jayway.restassured.specification.RequestSpecification;

public class MainTest extends AbstractITest {

	public static int testGroupCount = 0;

	@BeforeAll
	public static void setUp() throws Exception {
		abstractSetUp();
	}

	@TestFactory
	public Stream<DynamicTest> testWithRestAssured() throws Exception {

		List<YamlTestGroup> testGroups = getTestGroupData();
		Collection<DynamicTest> dynamicTests = new ArrayList<DynamicTest>();

		for (YamlTestGroup testGroup : testGroups) {

			if (testGroup.isSkip()) {
				logger.info("->skipped testGroup name=" + testGroup.getName());
				continue;
			}

			for (YamlTest test : testGroup.getTests()) {
				if (test.isSkip()) {
					logger.info("-->skipped test =" + test.getName() + ", testGroup =" + testGroup.getName());
					continue;
				}

				final String testcaseName = "testGroup=" + testGroup.getName() + "->test=" + test.getName();
				RequestSpecification rs = given().spec(rspec);
				Executable exec = () -> {

					logger.info("\n\n-->start " + testcaseName);
					try {
						RestRequest.build(rs, test).request().doAssert();
					} catch (Throwable e) {
						e.printStackTrace();
						throw e;
					}
					logger.info("-->end " + testcaseName);
				};

				DynamicTest dTest = DynamicTest.dynamicTest(testcaseName, exec);
				dynamicTests.add(dTest);

			}
		}

		return dynamicTests.stream();
	}

}
