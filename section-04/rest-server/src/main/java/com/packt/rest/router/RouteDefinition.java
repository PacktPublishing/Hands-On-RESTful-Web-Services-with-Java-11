package com.packt.rest.router;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class RouteDefinition {

    private String verb;
    private List<String> pathBits;

    public RouteDefinition(String route) {
        String[] parts = route.split(" ");
        verb = parts[0];
        String rest = Arrays.stream(parts).skip(1L).collect(joining(" "));
        pathBits = splitUri(rest);
    }

    private List<String> splitUri(String rest) {
        return Arrays.stream(rest.split("/")).filter(e -> !e.isEmpty()).collect(toList());
    }

    public boolean matches(HttpServletRequest req) {
        return matches(req.getMethod(), req.getRequestURI());
    }

    public boolean matches(String method, String uri) {
        if (!verb.equalsIgnoreCase(method)) {
            return false;
        }

        List<String> providedUri = splitUri(uri);

        if (providedUri.size() != pathBits.size()) {
            return false;
        }

        for (int i = 0; i < providedUri.size(); i++) {
            String providedBit = providedUri.get(i);
            String patternBit = pathBits.get(i);

            if (patternBit.startsWith(":")) {
                continue;
            }

            if (!patternBit.equalsIgnoreCase(providedBit)) {
                return false;
            }
        }

        return true;
    }
}
