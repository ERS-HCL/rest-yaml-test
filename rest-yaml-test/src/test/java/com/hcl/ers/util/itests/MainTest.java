package com.hcl.ers.util.itests;

import static com.jayway.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.Executable;

import com.hcl.ers.util.itests.beans.YamlInitGroup;
import com.hcl.ers.util.itests.beans.YamlTest;
import com.hcl.ers.util.itests.beans.YamlTestGroup;
import com.jayway.restassured.specification.RequestSpecification;

public class MainTest extends AbstractITest {

	public static YamlInitGroup initGroup;
	public static int testGroupCount = 0;

	@BeforeAll
	public static void setUp() {
		abstractSetUp();
		if (initGroup == null) {
			initGroup = getInitGroupData();
		}
	}
	
	@TestFactory
	public Stream<DynamicTest> testWithRestAssured() throws Exception {

		List<YamlTestGroup> testGroups = getTestGroupData();
		Collection<DynamicTest> dynamicTests = new ArrayList<DynamicTest>();

		for (YamlTestGroup testGroup : testGroups) {
			if (testGroup.isSkip()) {
				System.out.println("->skipped testGroup name=" + testGroup.getName());
				continue;
			}

			for (YamlTest test : testGroup.getTests()) {
				if (test.isSkip()) {
					System.out.println("-->skipped test =" + test.getName()+ ", testGroup ="+testGroup.getName());
					continue;
				}
				
				final String testcaseName = "testGroup="+ testGroup.getName()+ "->test=" + test.getName();
				
				RequestSpecification rs = given().spec(rspec);
				Executable exec = () -> {
					
					System.out.println("\n\n-->start "+testcaseName);
					RestRequest.build(rs, test, initGroup).request().doAssert();
					System.out.println("-->end "+testcaseName);
				};
				
				DynamicTest dTest = DynamicTest.dynamicTest(testcaseName, exec);
				dynamicTests.add(dTest);
				
			}
		}
		
		return dynamicTests.stream();
	}

}
