package com.packt.rest.router;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.stream.Collectors;

public class RouteDefinition {

    private String method;
    private String[] bits;

    public RouteDefinition(String route) {
        String[] parts = route.split(" ");
        this.method = parts[0];
        String rest = Arrays.stream(parts).skip(1).collect(Collectors.joining(" "));
        this.bits = rest.split("/");
    }

    public boolean match(HttpServletRequest req) {
        return match(req.getMethod(), req.getRequestURI());
    }

    public boolean match(String method, String uri) {
        if (!this.method.equalsIgnoreCase(method)) {
            return false;
        }

        String[] matchBits = uri.split("/");

        if (matchBits.length != bits.length) {
            return false;
        }

        for (int i = 0; i < bits.length; i++) {
            String bit = bits[i];
            if (bit.startsWith(":")) {
                continue;
            }
            if (!bit.equalsIgnoreCase(matchBits[i])) {
                return false;
            }
        }

        return true;
    }
}
