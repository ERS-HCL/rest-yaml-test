package com.hcl.ers.util.itests.beans;

import java.util.Map;

public class Response {

	private Map<String, String> headers;
	private Map<String, String> cookies;
	private Map<String, String> variables;
	private ResponseBody body;
	private int status;
	
	
	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public ResponseBody getBody() {
		return body;
	}

	public void setBody(ResponseBody body) {
		this.body = body;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Map<String, String> getCookies() {
		return cookies;
	}

	public void setCookies(Map<String, String> cookies) {
		this.cookies = cookies;
	}

	public Map<String, String> getVariables() {
		return variables;
	}

	public void setVariables(Map<String, String> variables) {
		this.variables = variables;
	}

}
