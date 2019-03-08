package com.packt.rest.router;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

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

    public boolean matches(Request req) {
        return matches(req.getMethod(), req.getUri(), req::withPathParam);
    }

    public boolean matches(String method, String uri, BiConsumer<String, String> fn) {
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
                fn.accept(patternBit.substring(1), providedBit);
                continue;
            }

            if (!patternBit.equalsIgnoreCase(providedBit)) {
                return false;
            }
        }

        return true;
    }
}
