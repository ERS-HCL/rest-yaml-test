package com.hcl.ers.util.itests.util;

import static org.hamcrest.CoreMatchers.equalTo;

import org.junit.Assert;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.util.Eval;


public class Groovy {

	public static void main(String[] args) {
		Binding binding = new Binding();
		
		GroovyShell shell = new GroovyShell(binding) ;
		binding.setProperty("i", "avalue");
		shell.setProperty("i", "bval");
		System.out.println(binding.getProperty("i"));
		//System.out.println(shell.evaluate(" a $i a"));
		System.out.println(shell.evaluate(" println 1"));
		
		Object v1 = Eval.me("[2, 3, 5, 8, 13, 21, 34]");
		Object v2 = Eval.me("[2, 3, 5, 8, 13, 21, 34]");
		System.out.println(Eval.me("[2, 3, 5, 8, 13, 21, 34]"));
		
		Assert.assertThat(v1, equalTo(v2));
	}

}
