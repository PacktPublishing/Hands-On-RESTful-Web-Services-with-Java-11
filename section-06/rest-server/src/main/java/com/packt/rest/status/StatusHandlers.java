package com.packt.rest.status;

import com.packt.rest.router.Request;
import com.packt.rest.router.Response;
import com.packt.rest.todo.Todos;

import static com.packt.rest.Util.response;

public class StatusHandlers {

	public static void startExpensiveOperation(Request req, Response resp) {
		Status status = Statuses.createNewStatus();
		dispatchExpensiveOperation(status.getId());
		response(resp, 200, status);
	}

	public static void fetchCurrentStatus(Request req, Response resp) {
		String id = req.getPathParameter("id");
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
