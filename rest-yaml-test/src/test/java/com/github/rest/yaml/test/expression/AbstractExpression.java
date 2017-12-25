package com.github.rest.yaml.test.expression;

import java.util.List;

public abstract class AbstractExpression implements Expression {
	protected String data;
	protected String expression;
	protected List<String> args;
}
