package com.hcl.ers.util.itests.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {

	public static Object find(String pattern, String input) {
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(input);
		List<String> list = new ArrayList<String>();

		while (m.find()) {
			String matched = m.group();
			list.add(matched);
		}
		
		if(!list.isEmpty() && list.size()==1) {
			return list.get(0);
		}
		
		return list;
	}
}