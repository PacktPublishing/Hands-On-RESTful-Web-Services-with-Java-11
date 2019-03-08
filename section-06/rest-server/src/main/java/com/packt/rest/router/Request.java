package com.packt.rest.router;

import com.packt.rest.Util;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Request {

	private String method;
	private String uri;
	private Map<String, String> headers = new HashMap<>();
	private Map<String, String> pathParameters = new HashMap<>();
	private String body;

	public Request(String method, String uri) {
		this.method = method;
		this.uri = uri;
	}

	public Request(HttpServletRequest req) throws IOException {
		this.method = req.getMethod();
		this.uri = req.getRequestURI();

		for (Iterator it = req.getHeaderNames().asIterator(); it.hasNext(); ) {
			String headerName = it.next().toString();
			this.headers.put(headerName, req.getHeader(headerName));
		}

		ServletInputStream stream = req.getInputStream();
		if (stream != null) {
			this.body = Util.readInputStream(stream);
		}
	}

	public String getMethod() {
		return method;
	}

	public String getUri() {
		return uri;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public Map<String, String> getPathParameters() {
		return pathParameters;
	}

	public String getPathParameter(String name) {
		return pathParameters.get(name);
	}

	public Long getPathParameterAsLong(String name) {
		return Long.parseLong(getPathParameter(name));
	}

	public String getHeader(String name) {
		return headers.get(name);
	}

	public String getBody() {
		return body;
	}

	public Request withMethod(String method) {
		this.method = method;
		return this;
	}

	public Request withUri(String uri) {
		this.uri = uri;
		return this;
	}

	public Request withBody(String body) {
		this.body = body;
		return this;
	}

	public Request withHeader(String name, String value) {
		this.headers.put(name, value);
		return this;
	}

	public Request withPathParam(String name, String value) {
		this.pathParameters.put(name, value);
		return this;
	}
}
