package com.packt.rest;

import com.packt.rest.router.RouteDefinition;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;

public class RouteDefinitionTest {
	@Test
	public void testSimpleRoute() {
		RouteDefinition route = new RouteDefinition("GET /todos");

		assertThat(route.matches("GET", "/todos")).isEqualTo(true);
		assertThat(route.matches("POST", "/todos")).isEqualTo(false);
		assertThat(route.matches("GET", "/todos/1")).isEqualTo(false);
	}

	@Test
	public void testRoutesWithParameters() {
		RouteDefinition route = new RouteDefinition("POST /todos/:id");

		assertThat(route.matches("POST", "/todos/1")).isEqualTo(true);
		assertThat(route.matches("POST", "/todos/bar")).isEqualTo(true);
		assertThat(route.matches("POST", "/todos/1/foo")).isEqualTo(false);
		assertThat(route.matches("GET", "/todos/1")).isEqualTo(false);
		assertThat(route.matches("POST", "/todos")).isEqualTo(false);
	}
}
