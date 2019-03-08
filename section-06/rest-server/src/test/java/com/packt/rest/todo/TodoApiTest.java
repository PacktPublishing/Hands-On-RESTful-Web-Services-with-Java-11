package com.packt.rest.todo;

import com.packt.rest.base.BaseApiTest;
import com.packt.rest.router.Response;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TodoApiTest extends BaseApiTest {

	@Test
	public void testListEmptyTodos() {
		Response resp = request("GET", "/todos");
		assertThat(resp.getStatus()).isEqualTo(200);
		assertThat(resp.getBody()).isEqualTo("[]");
	}

}
