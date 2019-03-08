package com.packt.rest.auth;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig) {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		String token = req.getHeader("Authorization");

		String actualToken = "very_secret_stuff"; // TODO in actual application, fetch this from something
		boolean isVerified = actualToken.equals(token);

		if (isVerified) {
			chain.doFilter(request, response);
		} else {
			resp.getWriter().println("Sorry, authentication required.");
			resp.setStatus(401); // 401, 403
		}
	}

	@Override
	public void destroy() {
	}
}
