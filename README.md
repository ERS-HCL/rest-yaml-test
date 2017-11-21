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

3. Create testGroup. You can create multiple testGroups one for each test scenerio. One testGroup can have multiple REST requests defined as test inside the testGroup. 
   Examples:
      a. Create test groups
	     testGroup:
		   -
			  name: first test group
		      skip: true/false #if you want to skip this testGroup from execution set value to true/false
			
		   -
		      name: second test group
			  skip: false
		   .....
	   b. Create tests
          
          testGroup:
            -
             ....
             tests:
               -
                 name: first rest request
                 request:
                   uri: /cart/get/cart/${cartId} # request uri hostname and port is picked up from baseURI from
				   method: get/post/put/delete # use any of these values
				   application.properties file.
				   header: # create key value pairs of headers and its values
				     content-type: {$contentType}
				   cookie: # create key value pairs of cookies and its values
				   body: | # send following json as request payload.
				      {
					    "a":"b",
						"c":"d"
					  }
				   response:
				     status: {$status} # assert that http response status is 200
				     header: # add key value pairs to assert on header values
				      content-type: {$contentType} #assert that content-type header is application/json
					 body:
                       asserts: # add multiple assert key value pairs 
					    -
                          jsonPath: args.foo # json path expression to be used to get value from response body
                          value: "v1,v2" # if the return value from json path is list of atomic values you can match them with comma separated values like this.
						  
                        -
                          jsonPath: args # json path expression
                          match: strict # if the return value from json path is json then JsonAssert is used to match expected and actual values refer http://jsonassert.skyscreamer.org/cookbook.html
                          value: | # expected json value
                              {
                                "foo": [
                                   "v1",
                                   "v2"
                                ]
                              }
                        -
                           jsonPath: headers.Host #json path expression
                           value: "httpbin.org" #if the json path returns single value then you can match it like this.