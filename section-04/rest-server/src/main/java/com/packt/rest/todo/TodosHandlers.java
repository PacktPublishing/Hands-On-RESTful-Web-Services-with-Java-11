package com.packt.rest.todo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.packt.rest.Util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class TodosHandlers {

    private static final Gson GSON = new GsonBuilder().create();

    public static void listTodos(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        response(resp, 200, Todos.list().stream().map(todo -> {
            Map<String, Object> result = new HashMap<>();
            result.put("id", todo.getId());
            result.put("text", todo.getText());
            result.put("textUpper", todo.getText().toUpperCase());
            return result;
        }).collect(toList()));
    }

    public static void fetchTodo(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String uri = req.getRequestURI();
        Long id = Long.parseLong(uri.substring("/todos/".length()));

        Todo todo = Todos.fetch(id);
        if (todo == null) {
            resp.setStatus(404);
        } else {
            response(resp, 200, todo);
        }
    }

    public static void createTodoWithoutId(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Todo todo = parseTodoFromRequest(req, null);
        Todos.update(todo);
        response(resp, 201, todo);
    }

    public static void createTodoWithId(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String uri = req.getRequestURI();
        Long id = Long.parseLong(uri.substring("/todos/".length()));

        if (Todos.exists(id)) {
            resp.setStatus(422);
            resp.getOutputStream().println("You cannot created Todo with id " + id + " because it exists!");
        }

        Todo todo = parseTodoFromRequest(req, id);
        Todos.update(todo);
        response(resp, 201, todo);
    }

    public static void updateTodo(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String uri = req.getRequestURI();
        Long id = Long.parseLong(uri.substring("/todos/".length()));

        if (!Todos.exists(id)) {
            resp.setStatus(422);
            resp.getOutputStream().println("You cannot update Todo with id " + id + " because it doesn't exists!");
        }

        Todo todo = parseTodoFromRequest(req, id);
        Todos.update(todo);
        response(resp, 200, todo);
    }

    public static void deleteTodo(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String uri = req.getRequestURI();
        Long id = Long.parseLong(uri.substring("/todos/".length()));

        Todo todo = Todos.delete(id);
        response(resp, 200, todo);
    }

    private static Todo parseTodoFromRequest(HttpServletRequest req, Long id) throws IOException {
        String json = Util.readInputStream(req.getInputStream());
        Todo todo = GSON.fromJson(json, Todo.class);
        todo.setId(id);
        return todo;
    }

    private static void response(HttpServletResponse resp, int status, Object obj) throws IOException {
        resp.setStatus(status);
        resp.setHeader("Content-Type", "application/json");
        resp.getOutputStream().println(GSON.toJson(obj));
    }
}
