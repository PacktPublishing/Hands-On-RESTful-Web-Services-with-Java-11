package com.packt.rest.todo;

import com.packt.rest.router.Request;
import com.packt.rest.router.Response;

import static com.packt.rest.Util.GSON;
import static com.packt.rest.Util.response;
import static java.util.stream.Collectors.toList;

public class TodosHandlers {

	public static void listTodos(Request req, Response resp) {
		response(resp, 200, Todos.list().stream().map(TodoDTO::new).collect(toList()));
	}

	public static void fetchTodo(Request req, Response resp) {
		Long id = req.getPathParameterAsLong("id");
		Todo todo = Todos.fetch(id);
		if (todo == null) {
			resp.setStatus(404);
		} else {
			response(resp, 200, new TodoDTO(todo));
		}
	}

	public static void createTodoWithoutId(Request req, Response resp) {
		Todo todo = parseTodoFromRequest(req, null);
		Todos.update(todo);
		response(resp, 201, new TodoDTO(todo));
	}

	public static void createTodoWithId(Request req, Response resp) {
		Long id = req.getPathParameterAsLong("id");

		if (Todos.exists(id)) {
			resp.setStatus(422);
			resp.setBody("You cannot created Todo with id " + id + " because it exists!");
			return;
		}

		Todo todo = parseTodoFromRequest(req, id);
		Todos.update(todo);
		response(resp, 201, new TodoDTO(todo));
	}

	public static void updateTodo(Request req, Response resp) {
		Long id = req.getPathParameterAsLong("id");

		if (!Todos.exists(id)) {
			resp.setStatus(422);
			resp.setBody("You cannot update Todo with id " + id + " because it doesn't exists!");
			return;
		}

		Todo todo = parseTodoFromRequest(req, id);
		Todos.update(todo);
		response(resp, 200, new TodoDTO(todo));
	}

	public static void deleteTodo(Request req, Response resp) {
		Long id = req.getPathParameterAsLong("id");

		Todo todo = Todos.delete(id);
		response(resp, 200, new TodoDTO(todo));
	}

	private static Todo parseTodoFromRequest(Request req, Long id) {
		Todo todo = GSON.fromJson(req.getBody(), Todo.class);
		todo.setId(id);
		return todo;
	}
}
