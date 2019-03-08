package com.packt.rest.task;

import com.packt.rest.router.Request;
import com.packt.rest.router.Response;

import static com.packt.rest.Util.GSON;
import static com.packt.rest.Util.response;

public class TasksHandler {

	public static void listTasks(Request req, Response resp) {
		Long todoId = req.getPathParameterAsLong("todoId");
		response(resp, 200, Tasks.list(todoId));
	}

	public static void fetchTask(Request req, Response resp) {
		Long todoId = req.getPathParameterAsLong("todoId");
		Long taskId = req.getPathParameterAsLong("taskId");

		Task task = Tasks.fetch(todoId, taskId);
		if (task == null) {
			resp.setStatus(404);
		} else {
			response(resp, 200, task);
		}
	}

	public static void createTaskWithoutTaskId(Request req, Response resp) {
		Long todoId = req.getPathParameterAsLong("todoId");

		Task task = parseTaskFromRequest(req);
		task.setTodoId(todoId);
		Tasks.update(task);
		response(resp, 201, task);
	}

	public static void updateTask(Request req, Response resp) {
		Long todoId = req.getPathParameterAsLong("todoId");
		Long taskId = req.getPathParameterAsLong("taskId");

		if (!Tasks.exists(todoId, taskId)) {
			resp.setStatus(422);
			resp.setBody("You cannot update a task that doesn't exists!");
			return;
		}

		Task task = parseTaskFromRequest(req);
		task.setTodoId(todoId);
		task.setId(taskId);
		Tasks.update(task);
		response(resp, 200, task);
	}

	public static void createTaskWithTaskId(Request req, Response resp) {
		Long todoId = req.getPathParameterAsLong("todoId");
		Long taskId = req.getPathParameterAsLong("taskId");

		if (Tasks.exists(todoId, taskId)) {
			resp.setStatus(422);
			resp.setBody("You cannot created Task with existing id");
			return;
		}

		Task task = parseTaskFromRequest(req);
		task.setId(taskId);
		task.setTodoId(todoId);
		Tasks.update(task);
		response(resp, 201, task);
	}

	public static void deleteTask(Request req, Response resp) {
		Long todoId = req.getPathParameterAsLong("todoId");
		Long taskId = req.getPathParameterAsLong("taskId");
		Task task = Tasks.delete(todoId, taskId);
		response(resp, 200, task);
	}

	private static Task parseTaskFromRequest(Request req) {
		return GSON.fromJson(req.getBody(), Task.class);
	}
}
