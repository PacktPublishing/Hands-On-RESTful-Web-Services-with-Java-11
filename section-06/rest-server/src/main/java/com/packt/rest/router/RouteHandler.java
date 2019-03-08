package com.packt.rest.router;

@FunctionalInterface
public interface RouteHandler {
    void execute(Request req, Response resp);
}
