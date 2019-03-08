package com.packt.rest.status;

import com.packt.rest.todo.Todos;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.packt.rest.Util.response;

public class StatusHandlers {

	public static void startExpensiveOperation(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Status status = Statuses.createNewStatus();
		dispatchExpensiveOperation(status.getId());
		response(resp, 200, status);
	}

	public static void fetchCurrentStatus(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String uri = req.getRequestURI();
		String id = uri.substring("/status/".length());

		Status status = Statuses.byId(id);
		response(resp, 200, status);
	}

	private static void dispatchExpensiveOperation(String id) {
		new Thread(() -> {
			int result = Todos.expensiveOperation();
			Statuses.updateStatus(id, result);
		}).start();
	}
}
