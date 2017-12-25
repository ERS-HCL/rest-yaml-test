package com.github.rest.yaml.test.expression;

import java.util.List;

import com.github.rest.yaml.test.util.TestException;
import com.jayway.jsonpath.JsonPath;

public class JsonExpression extends AbstractExpression {
	
	private JsonExpression(String data, String expression, List<String> args) {
		this.data = data;
		this.expression = expression;
		this.args = args;
	}

	public static JsonExpression build(String data, String expression, List<String> args) {
		if(!expression.equalsIgnoreCase(ExpressionType.jsonpath.toString())) {
			throw new TestException("Internal code exception expression=" + expression + " not implemented by JsonExpression class.");
		}
		return new JsonExpression(data, expression, args);
	}

	@Override
	public Object parse() {
		//There will be only one argument of JsonExpression
		return JsonPath.parse(data).read(args.get(0));
	}
	
}
