package com.packt.rest.router;

import com.packt.rest.Util;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Response {

	private HttpServletResponse resp;
	private Map<String, String> headers = new HashMap<>();
	private int status;
	private String body;

	public Response() {}

	public Response(HttpServletResponse resp) {
		this.resp = resp;
	}

	public Response setStatus(int status) {
		this.status = status;
		return this;
	}

	public Response setBody(String body) {
		this.body = body;
		return this;
	}

	public Response setBodyAsJson(Object obj) {
		return setBody(Util.GSON.toJson(obj));
	}

	public Response addHeader(String name, String value) {
		headers.put(name, value);
		return this;
	}

	public void send() throws IOException {
		headers.forEach(resp::addHeader);
		resp.setStatus(status);
		resp.getWriter().print(body);
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public String getHeader(String name) {
		return headers.get(name);
	}

	public int getStatus() {
		return status;
	}

	public String getBody() {
		return body;
	}
}
