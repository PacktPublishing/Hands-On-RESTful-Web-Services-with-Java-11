package com.packt.rest.task;

import com.packt.rest.database.DB;
import org.dalesbred.query.SqlQuery;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.packt.rest.Util.updateAndGetGeneratedId;
import static org.dalesbred.query.SqlQuery.namedQuery;

public class Tasks {

	public static List<Task> list(Long todoId) {
		return DB.db.findAll(Task.class, namedQuery("SELECT * FROM Tasks WHERE todo_id = :todoId", Map.of("todoId", todoId)));
	}

	public static Task fetch(Long todoId, Long taskId) {
		return getOptionalTask(todoId, taskId).orElse(null);
	}

	public static boolean exists(Long todoId, Long taskId) {
		return getOptionalTask(todoId, taskId).isPresent();
	}

	private static Optional<Task> getOptionalTask(Long todoId, Long taskId) {
		SqlQuery query = namedQuery("SELECT * FROM Tasks WHERE id = :taskId AND todo_id = :todoId", Map.of("todoId", todoId, "taskId", taskId));
		return DB.db.findOptional(Task.class, query);
	}

	public static Task update(Task task) {
		if (task.getId() == null) {
			SqlQuery query = namedQuery("INSERT INTO Tasks (todo_id, text, done) VALUES (:todoId, :text, :done)", task);
			Long id = updateAndGetGeneratedId(query);
			task.setId(id);
		} else {
			DB.db.update(namedQuery("UPDATE Tasks SET text = :text, done = :done WHERE id = :id", task));
		}
		return task;
	}

	public static Task delete(Long todoId, Long taskId) {
		Task task = fetch(todoId, taskId);
		DB.db.update(namedQuery("DELETE FROM Tasks WHERE id = :taskId AND todo_id = :todoId", Map.of("todoId", todoId, "taskId", taskId)));
		return task;
	}
}
