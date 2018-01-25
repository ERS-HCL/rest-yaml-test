package com.github.rest.yaml.test.util;

public class Logger {
	private String logLevel = "debug";
	public static final String ERROR = "error";
	public static final String INFO = "info";
	public static final String DEBUG = "debug";
	
	public Logger() {
		logLevel = Environment.instance().getLogLevel();
	}
	
	/**
	 * Always log messages irrespective of logLevel
	 * @param message
	 */
	public void log(String message) {
		System.out.print(message);
	}
	
	public void debug(String message) {
		if (logLevel.equals("debug")) {
			System.out.print(message);
		}
	}
	
	public void error(String message) {
		if (logLevel.equals("debug") || logLevel.equals("info") || logLevel.equals("error")) {
			System.out.print(message);
		}
	}
	
	public void info(String message) {
		if (logLevel.equals("info") || logLevel.equals("debug")) {
			System.out.print(message);
		}
	}

}
