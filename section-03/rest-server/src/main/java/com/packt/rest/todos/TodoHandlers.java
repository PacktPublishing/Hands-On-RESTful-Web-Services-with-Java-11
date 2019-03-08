package com.packt.rest.todos;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.packt.rest.Util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TodoHandlers {

    private static final Gson GSON = new GsonBuilder().create();

    public static void listTodos(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        response(resp, 200, Todos.todos.values());
    }

    public static void getTodo(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String uri = req.getRequestURI();
        Long id = parseIdFromUrl(uri);

        Todo todo = Todos.todos.get(id);
        response(resp, 200, todo);
    }

    public static void createTodoWithoutId(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Todo todo = parseTodo(req, Todos.nextId());
        Todos.todos.put(todo.getId(), todo);

        response(resp, 201, todo);
    }

    public static void createTodoWithId(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String uri = req.getRequestURI();
        Long id = parseIdFromUrl(uri);

        if (Todos.todos.containsKey(id)) {
            resp.setStatus(422);
            resp.getOutputStream().println("You cannot created Todo with id " + id + " because it exists!");
        }

        Todo todo = parseTodo(req, id);
        Todos.todos.put(todo.getId(), todo);
        response(resp, 201, todo);
    }

    public static void updateTodo(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String uri = req.getRequestURI();
        Long id = parseIdFromUrl(uri);

        if (!Todos.todos.containsKey(id)) {
            resp.setStatus(422);
            resp.getOutputStream().println("You cannot update Todo with id " + id + " because it doesn't exists!");
        }

        Todo todo = parseTodo(req, id);
        Todos.todos.put(todo.getId(), todo);
        response(resp, 200, todo);
    }

    public static void removeTodo(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String uri = req.getRequestURI();
        Long id = parseIdFromUrl(uri);

        Todo todo = Todos.todos.remove(id);
        response(resp, 200, todo);
    }

    private static Long parseIdFromUrl(String uri) {
        return Long.parseLong(uri.substring("/todos/".length()));
    }

    private static Todo parseTodo(HttpServletRequest req, Long id) throws IOException {
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
