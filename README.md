Test your REST API by writing test data in YAML only without writing any code.

How to start?
This is a Java Maven project. Follow these steps.
1. Import this project into your eclipse workspace.
2. 1. A new test data file is required for each environment if you want to run your tests against different environments e.g. dev, test
3. Test data file name should be in "test-data-<environment-name>.yaml" format where "environment-name" is the name of environment.
4. Create new application configuration file with "application-<environment-name>.properties" and change "server.port", "server.baseURI".
3. Write test data into "test-data-<environment-name>.yaml"
4. mvn clean verify <environment-name> from command window.
5. If you want to run test from eclipse then right click "MainTest" class and run it as junit test.

How to write test cases?
1. Please read "src/test/resources/test-data-dev.yaml" test case file.
2. Define variables. Variables can be defined inside the initGroup and they have global scope. that means you use these variables anywhere. You can reference these variables like ${variableName} to assign values to a header, a cookie and in a request uri. You can also assign values to these variables from REST response and refer the value in subsequent tests.
    Example:
	a. define variables
	    initGroup:
		  variables:
		     status: 200 #status variable is created with intial value of 200 
			 contentType: "application/json" # contentType variable is created with initial value of application/json
			 cartId: cid-1001
			 uuid: # create uuid variable with null value
			 country: US
	b. use variables
	    testGroup:
		  -
		    name: ....
		    tests:
			  -
			    request:
				  uri: /get/cart/${cartId} # this is how you can use a variable inside uri.
				  headers:
				    content-type: {$contentType} # send content-type=application/json header in the request
				  cookies:
				    country: {$country} # send country=US cookie in the request
				  ....
				response:
				  status: {$status} # assert that http response status is 200
				  header:
				    content-type: {$contentType} #assert that content-type header is application/json
					
	c. assign value to a variable from response.
	    testGroup:
		  -
		    name: ....
		    tests:
			  -
			    request:
				  ....
				response:
				  ....
				  ....
				  variables:
				    uuid: header.uuid # assign uuid header value to uuid variable defined in the initGroup. if variable not defined new variable will be created.
					sessionId:  cookie.session-id # assign session-id cookie value to sessionId variable.
					skuId: body.response.skuId # assign value extracted from json response using "response.skuId" JsonPath expression for json path expression refer https://github.com/json-path/JsonPath
