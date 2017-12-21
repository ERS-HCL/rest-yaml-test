package com.github.rest.yaml.test.util;

public class Logger {
	private boolean logDebug;

	public Logger() {
		logDebug = Environment.instance().getLogDebug();
	}

	public void debug(String message) {
		if (logDebug) {
			System.out.print(message);
		}
	}

	public void info(String message) {
		System.out.println(message);
	}

}
