package com.github.rest.yaml.test.expression;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.rest.yaml.test.util.TestException;

public class RegexExpression extends AbstractExpression {
	
	private RegexExpression(String data, String expression, List<String> args) {
		this.data = data;
		this.expression = expression;
		this.args = args;
	}

	public static RegexExpression build(String data, String expression, List<String> args) {
		if(!expression.toLowerCase().startsWith(ExpressionType.regex.toString())) {
			throw new TestException("Internal code exception expression=" + expression + " not implemented by RegexExpression class.");
		}
		return new RegexExpression(data, expression, args);
	}
	
	public static Object find(String input, String pattern) {
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(input);
		List<String> list = new ArrayList<String>();

		while (m.find()) {
			String matched = m.group();
			list.add(matched);
		}

		return list;
	}

	public static int match(String input, String pattern) {
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(input);
		int matchCount = 0;

		while (m.find()) {
			matchCount = matchCount + 1;
		}

		return matchCount;
	}

	@Override
	public Object parse() {
		if(expression.equalsIgnoreCase(ExpressionType.regex_find.toString())) {
			//Only one argument is required.
			return find(data, args.get(0));
		} else if (expression.equalsIgnoreCase(ExpressionType.regex_match.toString())) {
			//Only one argument is required.
			return match(data, args.get(0));
		} else {
			throw new TestException("Internal code exception expression=" + expression + " not implemented by RegexExpression class.");
		}
		
	}
}
