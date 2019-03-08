package com.packt.rest.task;

import com.packt.rest.Util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.packt.rest.Util.GSON;
import static com.packt.rest.Util.response;

public class TasksHandler {

	public static void listTasks(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Long todoId = Long.parseLong(req.getAttribute("todoId").toString());
		response(resp, 200, Tasks.list(todoId));
	}

	public static void fetchTask(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Long todoId = Long.parseLong(req.getAttribute("todoId").toString());
		Long taskId = Long.parseLong(req.getAttribute("taskId").toString());

		Task task = Tasks.fetch(todoId, taskId);
		if (task == null) {
			resp.setStatus(404);
		} else {
			response(resp, 200, task);
		}
	}

	public static void createTaskWithoutTaskId(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Long todoId = Long.parseLong(req.getAttribute("todoId").toString());

		Task task = parseTaskFromRequest(req);
		task.setTodoId(todoId);
		Tasks.update(task);
		response(resp, 201, task);
	}

	public static void updateTask(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Long todoId = Long.parseLong(req.getAttribute("todoId").toString());
		Long taskId = Long.parseLong(req.getAttribute("taskId").toString());

		if (!Tasks.exists(todoId, taskId)) {
			resp.setStatus(422);
			resp.getOutputStream().println("You cannot update a task that doesn't exists!");
		}

		Task task = parseTaskFromRequest(req);
		task.setTodoId(todoId);
		task.setId(taskId);
		Tasks.update(task);
		response(resp, 200, task);
	}

	public static void createTaskWithTaskId(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Long todoId = Long.parseLong(req.getAttribute("todoId").toString());
		Long taskId = Long.parseLong(req.getAttribute("taskId").toString());

		if (Tasks.exists(todoId, taskId)) {
			resp.setStatus(422);
			resp.getOutputStream().println("You cannot created Task with existing id");
		}

		Task task = parseTaskFromRequest(req);
		task.setId(taskId);
		task.setTodoId(todoId);
		Tasks.update(task);
		response(resp, 201, task);
	}

	public static void deleteTask(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Long todoId = Long.parseLong(req.getAttribute("todoId").toString());
		Long taskId = Long.parseLong(req.getAttribute("taskId").toString());
		Task task = Tasks.delete(todoId, taskId);
		response(resp, 200, task);
	}

	private static Task parseTaskFromRequest(HttpServletRequest req) throws IOException {
		String json = Util.readInputStream(req.getInputStream());
		return GSON.fromJson(json, Task.class);
	}
}
