package com.github.rest.yaml.test.selector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import com.github.rest.yaml.test.beans.YamlBodyAssert;
import com.github.rest.yaml.test.util.JsonMapper;
import com.github.rest.yaml.test.util.Regex;
import com.github.rest.yaml.test.util.TestException;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.jayway.jsonpath.JsonPath;
import com.jayway.restassured.response.Response;

public class BodyAssertSelector {

	private YamlBodyAssert bodyAssert;

	private BodyAssertSelector(YamlBodyAssert bodyAssert) {
		this.bodyAssert = bodyAssert;
	}

	public static BodyAssertSelector build(YamlBodyAssert bodyAssert) {
		return new BodyAssertSelector(bodyAssert);
	}

	public Object eval(Response response) {
		Object value = null;
		String data = response.body().asString();

		if (bodyAssert.getSelect() != null) {
			Multimap<String, List<String>> exps = parsedExpressions(bodyAssert.getSelect());

			for (Entry<String, List<String>> entry : exps.entries()) {
				if (entry.getKey().equalsIgnoreCase(YamlBodyAssert.ExpressionType.jsonpath.toString())) {
					value = JsonPath.parse(data).read(entry.getValue().get(0));
				} else if (entry.getKey().equalsIgnoreCase(YamlBodyAssert.ExpressionType.regex_find.toString())) {
					value = Regex.find(entry.getValue().get(0), data);
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

	@SuppressWarnings("unlikely-arg-type")
	private void validateExpression(String expression) {
		List<String> args = tokinize(expression, " ");
		
		// examples "jsonpath json-expression" or "regex v\d" or "string.startsWith abc"
		// or "string.matches v\d"
		if (args.isEmpty() || args.size() < 2 || !YamlBodyAssert.ExpressionType.exist(args.get(0))) {
			throw new TestException("Expression=" + expression + " is not not support in select=" + bodyAssert.getSelect());
		}
	}

}
