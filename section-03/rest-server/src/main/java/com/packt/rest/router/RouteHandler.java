package com.packt.rest.router;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@FunctionalInterface
public interface RouteHandler {
    void execute(HttpServletRequest req, HttpServletResponse resp) throws IOException;
}
