package com.packt.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.packt.rest.database.DB;
import org.dalesbred.query.SqlQuery;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.joining;

public class Util {

	public static final Gson GSON = new GsonBuilder().create();

	public static String readInputStream(InputStream stream) {
		return new BufferedReader(new InputStreamReader(stream)).lines().collect(joining("\n"));
	}

	public static void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public static String uuid() {
		return UUID.randomUUID().toString();
	}

	public static void response(HttpServletResponse resp, int status, Object obj) throws IOException {
		resp.setStatus(status);
		resp.setHeader("Content-Type", "application/json");
		resp.getOutputStream().println(GSON.toJson(obj));
	}

	public static Long updateAndGetGeneratedId(SqlQuery query) {
		return DB.db.updateAndProcessGeneratedKeys(result -> {
			result.next();
			return result.getLong(1);
		}, List.of("id"), query);
	}
}
