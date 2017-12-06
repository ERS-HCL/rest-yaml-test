package com.hcl.ers.util.itests.beans;

import java.util.Map;

public class YamlRequest {

	private String uri;
	private String method;
	private Map<String, String> headers;
	private String body;
	private Map<String, String> parameters;
	private Map<String, String> cookies;
	private boolean encodeURL;
	
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	public Map<String, String> getCookies() {
		return cookies;
	}

	public void setCookies(Map<String, String> cookies) {
		this.cookies = cookies;
	}

	public boolean isEncodeURL() {
		return encodeURL;
	}

	public void setEncodeURL(boolean encodeURL) {
		this.encodeURL = encodeURL;
	}
	
}
