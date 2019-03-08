package com.packt.rest.todo;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.packt.rest.Util;
import com.packt.rest.database.DB;
import org.dalesbred.query.SqlQuery;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.packt.rest.Util.updateAndGetGeneratedId;
import static org.dalesbred.query.SqlQuery.namedQuery;

public class Todos {

	private static final LoadingCache<Long, Optional<Todo>> todosCache = CacheBuilder.newBuilder()
			.maximumSize(100)
			.expireAfterWrite(20, TimeUnit.SECONDS)
			.build(new CacheLoader<>() {
				@Override
				public Optional<Todo> load(Long key) {
					return doFetch(key);
				}
			});

	public static List<Todo> list() {
		return DB.db.findAll(Todo.class, "SELECT * FROM Todos");
	}

	public static Todo fetch(Long id) {
		return todosCache.getUnchecked(id).orElse(null);
	}

	public static Optional<Todo> doFetch(Long id) {
		return getOptionalTodo(id);
	}

	public static boolean exists(Long id) {
		return getOptionalTodo(id).isPresent();
	}

	private static Optional<Todo> getOptionalTodo(Long id) {
		SqlQuery query = namedQuery("SELECT * FROM Todos WHERE id = :id", Map.of("id", id));
		return DB.db.findOptional(Todo.class, query);
	}

	public static Todo update(Todo todo) {
		if (todo.getId() == null) {
			SqlQuery query = namedQuery("INSERT INTO Todos (text) VALUES (:text)", todo);
			Long id = updateAndGetGeneratedId(query);
			todo.setId(id);
		} else {
			todosCache.invalidate(todo.getId());
			DB.db.update(namedQuery("UPDATE Todos SET text = :text WHERE id = :id", todo));
		}
		return todo;
	}

	public static Todo delete(Long id) {
		Todo todo = fetch(id);
		todosCache.invalidate(id);
		DB.db.update(namedQuery("DELETE FROM Todos WHERE id = :id", Map.of("id", id)));
		return todo;
	}

	public static int expensiveOperation() {
		Util.sleep(15 * 1000);
		return 42;
	}
}
