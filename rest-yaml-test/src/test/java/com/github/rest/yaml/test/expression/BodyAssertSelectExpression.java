package com.github.rest.yaml.test.expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import com.github.rest.yaml.test.beans.YamlBodyAssert;
import com.github.rest.yaml.test.util.JsonMapper;
import com.github.rest.yaml.test.util.TestException;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.jayway.restassured.response.Response;

public class BodyAssertSelectExpression {

	private YamlBodyAssert bodyAssert;

	private BodyAssertSelectExpression(YamlBodyAssert bodyAssert) {
		this.bodyAssert = bodyAssert;
	}

	public static BodyAssertSelectExpression build(YamlBodyAssert bodyAssert) {
		return new BodyAssertSelectExpression(bodyAssert);
	}

	public Object eval(Response response) {
		Object value = null;
		String data = response.body().asString();

		if (bodyAssert.getSelect() != null) {
			Multimap<String, List<String>> exps = parsedExpressions(bodyAssert.getSelect());

			for (Entry<String, List<String>> entry : exps.entries()) {
				if (entry.getKey().toLowerCase().startsWith(ExpressionType.jsonpath.toString().toLowerCase())) {
					value = JsonExpression.build(data, entry.getKey(), entry.getValue()).parse();
				} else if (entry.getKey().toLowerCase().startsWith(ExpressionType.regex.toString().toLowerCase())) {
					value = RegexExpression.build(data, entry.getKey(), entry.getValue()).parse();
				} else {
					throw new TestException("Expression="+entry.getKey()+" not supported in select=" + bodyAssert.getSelect());
				}
				data = JsonMapper.toJson(value);
			}

		}

		return value;
	}

	private List<String> tokinize(String select, String delimiter) {
		StringTokenizer tok = new StringTokenizer(select, delimiter);
		List<String> operations = new ArrayList<>();
		while (tok.hasMoreTokens()) {
			operations.add(tok.nextToken().trim());
		}
		return operations;
	}

	private Multimap<String, List<String>> parsedExpressions(String select) {
		ListMultimap<String, List<String>> parsedExpressions = LinkedListMultimap.create();
		List<String> expressions = tokinize(select, "|");

		if (expressions.isEmpty()) {
			throw new TestException("Select expression is not valid select=" + bodyAssert.getSelect());
		}

		for (String expression : expressions) {
			validateExpression(expression);
			List<String> args = tokinize(expression, " ");
			parsedExpressions.put(args.get(0), args.subList(1, args.size()));
		}

		return parsedExpressions;
	}

	private void validateExpression(String expression) {
		List<String> args = tokinize(expression, " ");
		
		// examples "jsonpath json-expression" or "regex v\d" or "string.startsWith abc"
		// or "string.matches v\d"
		if (args.isEmpty() || args.size() < 2 || !ExpressionType.exist(args.get(0))) {
			throw new TestException("Expression=" + expression + " is not not support in select=" + bodyAssert.getSelect());
		}
	}

}
