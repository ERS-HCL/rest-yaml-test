package com.github.rest.yaml.test.util;

import java.util.Arrays;
import java.util.List;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class Environment {

	private static final String ENV = "env";
	private Config conf;
	private int port;
	private String baseURL;
	private boolean logDebug;
	private List<String> testFiles;
	private List<String> certificates;
	private static Environment env;

	public static Environment instance() {
		if (env == null) {
			env = new Environment();
		}
		return env;
	}

	private Environment() {
		String env = getEnv();
		if (env == null) {
			conf = ConfigFactory.load("configuration");
		} else {
			conf = ConfigFactory.load("configuration-" + env);
		}

		baseURL = conf.getString("server.baseURI");
		port = conf.getInt("server.port");
		logDebug = conf.getBoolean("logDebug");
		testFiles = Arrays.asList(conf.getString("testFiles").split("[\\s,]+"));
		certificates = Arrays.asList(conf.getString("certificates").split("[\\s,]+"));
	}

	public boolean getLogDebug() {
		return logDebug;
	}

	public String getBaseURL() {
		return baseURL;
	}

	public int getPort() {
		return port;
	}

	public List<String> getTestFiles() {
		return testFiles;
	}
	
	public List<String> getCertificates() {
		return certificates;
	}
	
	private String getEnv() {
		final String env = System.getProperty(ENV);
		if (env != null) {
			return env;
		} else {
			return null;
		}
	}
}
