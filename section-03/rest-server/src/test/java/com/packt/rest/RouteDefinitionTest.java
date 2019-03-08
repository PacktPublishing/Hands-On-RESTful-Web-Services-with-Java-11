package com.packt.rest;

import com.packt.rest.router.RouteDefinition;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RouteDefinitionTest {

    @Test
    public void testSimpleRoute() {
        RouteDefinition def = new RouteDefinition("GET /todos");

        assertThat(def.match("POST", "/todos")).isEqualTo(false);
        assertThat(def.match("GET", "/todos")).isEqualTo(true);
        assertThat(def.match("GET", "/todos/1")).isEqualTo(false);
    }

    @Test
    public void testRouteWithParameter() {
        RouteDefinition def = new RouteDefinition("POST /todos/:id");

        assertThat(def.match("GET", "/todos/1")).isEqualTo(false);
        assertThat(def.match("POST", "/todos/1")).isEqualTo(true);
        assertThat(def.match("POST", "/todos/1/foo")).isEqualTo(false);
    }
}
