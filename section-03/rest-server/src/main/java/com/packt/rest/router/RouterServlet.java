package com.packt.rest.router;

import com.packt.rest.todos.TodoHandlers;
import com.packt.rest.users.UserHandlers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RouterServlet extends HttpServlet {

    private static final Map<RouteDefinition, RouteHandler> routes = new HashMap<>();

    private static void addRoute(String definition, RouteHandler handler) {
        routes.put(new RouteDefinition(definition), handler);
    }

    static {
        addRoute("GET /todos", TodoHandlers::listTodos);
        addRoute("GET /todos/:id", TodoHandlers::getTodo);
        addRoute("POST /todos", TodoHandlers::createTodoWithoutId);
        addRoute("POST /todos/:id", TodoHandlers::createTodoWithId);
        addRoute("PUT /todos/:id", TodoHandlers::updateTodo);
        addRoute("DELETE /todos/:id", TodoHandlers::removeTodo);

        addRoute("GET /users", UserHandlers::listUsers);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        genericHandler(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        genericHandler(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        genericHandler(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        genericHandler(req, resp);
    }

    private void genericHandler(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        for (Map.Entry<RouteDefinition, RouteHandler> route : routes.entrySet()) {
            if (route.getKey().match(req)) {
                route.getValue().execute(req, resp);
                return;
            }
        }

        // if nothing matches
        resp.setStatus(404);
    }
}
