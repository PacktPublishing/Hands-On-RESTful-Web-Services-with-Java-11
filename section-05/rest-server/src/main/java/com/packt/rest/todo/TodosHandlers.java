package com.packt.rest.todo;

import com.packt.rest.Util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.packt.rest.Util.GSON;
import static com.packt.rest.Util.response;
import static java.util.stream.Collectors.toList;

public class TodosHandlers {

	public static void listTodos(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		response(resp, 200, Todos.list().stream().map(TodoDTO::new).collect(toList()));
	}

	public static void fetchTodo(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Long id = Long.parseLong(req.getAttribute("id").toString());
		Todo todo = Todos.fetch(id);
		if (todo == null) {
			resp.setStatus(404);
		} else {
			response(resp, 200, new TodoDTO(todo));
		}
	}

	public static void createTodoWithoutId(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Todo todo = parseTodoFromRequest(req, null);
		Todos.update(todo);
		response(resp, 201, new TodoDTO(todo));
	}

	public static void createTodoWithId(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Long id = Long.parseLong(req.getAttribute("id").toString());

		if (Todos.exists(id)) {
			resp.setStatus(422);
			resp.getOutputStream().println("You cannot created Todo with id " + id + " because it exists!");
		}

		Todo todo = parseTodoFromRequest(req, id);
		Todos.update(todo);
		response(resp, 201, new TodoDTO(todo));
	}

	public static void updateTodo(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Long id = Long.parseLong(req.getAttribute("id").toString());

		if (!Todos.exists(id)) {
			resp.setStatus(422);
			resp.getOutputStream().println("You cannot update Todo with id " + id + " because it doesn't exists!");
		}

		Todo todo = parseTodoFromRequest(req, id);
		Todos.update(todo);
		response(resp, 200, new TodoDTO(todo));
	}

	public static void deleteTodo(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Long id = Long.parseLong(req.getAttribute("id").toString());
		Todo todo = Todos.delete(id);
		response(resp, 200, new TodoDTO(todo));
	}

	private static Todo parseTodoFromRequest(HttpServletRequest req, Long id) throws IOException {
		String json = Util.readInputStream(req.getInputStream());
		Todo todo = GSON.fromJson(json, Todo.class);
		todo.setId(id);
		return todo;
	}
}
