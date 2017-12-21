package com.github.rest.yaml.test.util;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class Environment {

	private static final String ENV = "env";
	private Config conf;
	private int port;
	private String baseURL;
	private boolean logDebug;

	private static Environment env;

	public static Environment instance() {
		if (env == null) {
			env = new Environment();
		}
		return env;
	}

	private Environment() {
		conf = ConfigFactory.load("application-" + getEnv());
		baseURL = conf.getString("server.baseURI");
		port = conf.getInt("server.port");
		logDebug = conf.getBoolean("logDebug");
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

	public String getEnv() {
		final String env = System.getProperty(ENV);
		if (env != null) {
			return env;
		} else {
			return "dev";
		}
	}
}
