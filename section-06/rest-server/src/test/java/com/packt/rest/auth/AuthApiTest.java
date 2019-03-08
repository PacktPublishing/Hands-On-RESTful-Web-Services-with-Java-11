package com.packt.rest.auth;

import com.packt.rest.base.BaseApiTest;
import com.packt.rest.router.Request;
import com.packt.rest.router.Response;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthApiTest extends BaseApiTest {

	@Test
	public void testNoAccessWithoutAuthorization() {
		Response resp = request(new Request("GET", "/todos"));
		assertThat(resp.getStatus()).isEqualTo(401);
	}

	@Test
	public void testDoHaveAccessWithAuthorization() {
		Response resp = request(new Request("GET", "/todos").withHeader("Authorization", "123"));
		assertThat(resp.getStatus()).isEqualTo(200);
	}
}
