package com.packt.rest.database;

import org.dalesbred.Database;

public class DB {

	public static final Database db;

	static {
		db = Database.forUrlAndCredentials("jdbc:postgresql://[::1]:5432/todo-api", "todo-api", "123");
	}
}