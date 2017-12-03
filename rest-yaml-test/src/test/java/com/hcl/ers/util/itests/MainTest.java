package com.hcl.ers.util.itests;

import static com.jayway.restassured.RestAssured.given;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.hcl.ers.util.itests.beans.YamlInitGroup;
import com.hcl.ers.util.itests.beans.YamlTest;
import com.hcl.ers.util.itests.beans.YamlTestGroup;
import com.jayway.restassured.specification.RequestSpecification;

public class MainTest extends AbstractITest {

	@Parameter(value = 0)
	public YamlTestGroup testGroup;
    public static YamlInitGroup initGroup;
    public static int testGroupCount=0;
    
    
	@Parameters(name = "{index}: test - {0} ")
	public static List<YamlTestGroup> data() throws Exception {
		return getTestGroupData();
	}

	@Before
	public void setUp() {
		super.setUp();
		if(initGroup == null) {
			this.initGroup = getInitGroupData();
		}
	}

	@Test
	public void testWithRestAssured() throws Exception {
		
		testGroupCount = testGroupCount+1;
		if(testGroup.isSkip()) {
			System.out.println("->skipped testGroup name="+testGroup.getName()+" testGroup count="+testGroupCount);
			return;
		}
		
		System.out.println("->star testGroup name="+testGroup.getName()+" testGroup count="+testGroupCount);
		
		int testCount = 0;
		for (YamlTest test : testGroup.getTests()) {
			testCount = testCount+1;
			if(test.isSkip()) {
				System.out.println("-->skipped test ="+test.getName()+" , test count="+testCount);
				continue;
			}
			
			System.out.println("-->start test ="+test.getName()+" , test count="+testCount);
			
			RequestSpecification rs = given().spec(rspec);
			RestRequest.build(rs, test, initGroup).request().doAssert();
			
			System.out.println("-->end test ="+test.getName()+" ,test count="+testCount);
		}
	}
	
}
