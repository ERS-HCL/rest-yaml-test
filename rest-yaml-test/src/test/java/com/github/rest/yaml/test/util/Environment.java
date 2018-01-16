package com.github.rest.yaml.test.util;

import java.util.Arrays;
import java.util.List;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;

public class Environment {

	private static final String ENV = "env";
	private static final String BASE_URL = "baseURL";
	private static final String PORT = "port";
	private static final String TAGS = "tags";
	
	private Config conf;
	private int port;
	private String baseURL;
	private boolean logDebug;
	private List<String> testFiles;
	private List<String> certificates;
	private List<String> tags;
	private static Environment env;

	public static Environment instance() {
		if (env == null) {
			env = new Environment();
		}
		return env;
	}

	private Environment() {

		if (getEnv(ENV) == null) {
			conf = ConfigFactory.load("configuration");
		} else {
			conf = ConfigFactory.load("configuration-" + env);
		}
		
		baseURL = getEnv(BASE_URL);
		if (baseURL == null) {
			baseURL = conf.getString("server.baseURI");
		}
		
		
		if(getEnv(PORT) == null) {
			port = conf.getInt("server.port");
		} else {
			try {
				port = Integer.parseInt(getEnv(PORT));
			} catch(NumberFormatException e) {
				throw new TestException("Port number should be number.", e);
			}
		}
		
		if(getEnv(TAGS) == null) {
			try {
				if (conf.getString(TAGS) != null) {
					tags = Arrays.asList(conf.getString(TAGS).split("[\\s,]+"));
				}
			} catch (ConfigException e) {
				// ignore
			}
		} else {
			tags = Arrays.asList(getEnv(TAGS).split("[\\s,]+"));
		}
		
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
	
	private String getEnv(String name) {
		final String env = System.getProperty(name);
		if (env != null) {
			return env;
		} else {
			return null;
		}
	}

	public List<String> getTags() {
		return tags;
	}
}
