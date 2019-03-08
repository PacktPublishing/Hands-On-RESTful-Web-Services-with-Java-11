package com.packt.rest.router;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static org.assertj.core.api.Assertions.assertThat;

public class RouteDefinitionTest {
	private static final BiConsumer<String, String> EMPTY_FN = (k, v) -> {};

	@Test
	public void testSimpleRoute() {
		RouteDefinition route = new RouteDefinition("GET /todos");

		assertThat(route.matches("GET", "/todos", EMPTY_FN)).isEqualTo(true);
		assertThat(route.matches("POST", "/todos", EMPTY_FN)).isEqualTo(false);
		assertThat(route.matches("GET", "/todos/1", EMPTY_FN)).isEqualTo(false);
	}

	@Test
	public void testRoutesWithParameters() {
		RouteDefinition route = new RouteDefinition("POST /todos/:id");

		assertThat(route.matches("POST", "/todos/1", EMPTY_FN)).isEqualTo(true);
		assertThat(route.matches("POST", "/todos/bar", EMPTY_FN)).isEqualTo(true);
		assertThat(route.matches("POST", "/todos/1/foo", EMPTY_FN)).isEqualTo(false);
		assertThat(route.matches("GET", "/todos/1", EMPTY_FN)).isEqualTo(false);
		assertThat(route.matches("POST", "/todos", EMPTY_FN)).isEqualTo(false);
	}

	@Test
	public void testParameters() {
		RouteDefinition route = new RouteDefinition("GET /todos/:todoId/tasks/:taskId");
		Map<String, String> attributes = new HashMap<>();

		boolean match = route.matches("GET", "/todos/123/tasks/456", attributes::put);
		assertThat(match).isEqualTo(true);

		assertThat(attributes.get("todoId")).isEqualTo("123");
		assertThat(attributes.get("taskId")).isEqualTo("456");
	}
}
