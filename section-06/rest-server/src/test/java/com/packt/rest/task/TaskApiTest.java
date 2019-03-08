package com.packt.rest.task;

import com.google.gson.reflect.TypeToken;
import com.packt.rest.Util;
import com.packt.rest.base.BaseApiTest;
import com.packt.rest.router.Response;
import com.packt.rest.todo.Todo;
import com.packt.rest.todo.Todos;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TaskApiTest extends BaseApiTest {

	@Test
	public void testListAfterCreateTask() {
		Long todoId = Todos.update(new Todo()).getId();

		Response postResp = request("POST", "/todos/" + todoId + "/tasks", "{ \"text\": \"do it\", \"done\": false }");
		assertThat(postResp.getStatus()).isEqualTo(201);

		Task task = Util.GSON.fromJson(postResp.getBody(), Task.class);
		assertThat(task.getTodoId()).isEqualTo(todoId);
		assertThat(task.getText()).isEqualTo("do it");
		assertThat(task.isDone()).isEqualTo(false);

		Response getResp = request("GET", "/todos/" + todoId + "/tasks");
		assertThat(getResp.getStatus()).isEqualTo(200);

		List<Task> tasks = Util.GSON.fromJson(getResp.getBody(), new TypeToken<List<Task>>() {}.getType());
		assertThat(tasks.size()).isEqualTo(1);
		assertThat(tasks.get(0).getText()).isEqualTo("do it");
	}
}
