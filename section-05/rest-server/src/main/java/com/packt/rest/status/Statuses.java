package com.packt.rest.status;

import com.packt.rest.Util;
import com.packt.rest.database.DB;
import org.dalesbred.query.SqlQuery;

import java.util.Map;
import java.util.Optional;

import static org.dalesbred.query.SqlQuery.namedQuery;

public class Statuses {

	public static Status byId(String id) {
		return getOptionalStatus(id).orElse(null);
	}

	private static Optional<Status> getOptionalStatus(String id) {
		SqlQuery query = namedQuery("SELECT * FROM Status WHERE id = :id", Map.of("id", id));
		return DB.db.findOptional(Status.class, query);
	}

	public static Status createNewStatus() {
		Status s = new Status();
		s.setId(Util.uuid());
		s.setStatus("loading");
		s.setResult(null);

		SqlQuery query = namedQuery("INSERT INTO Status (id, status, result) VALUES (:id, :status, :result)", s);
		DB.db.update(query);

		return s;
	}

	public static void updateStatus(String id, int result) {
		SqlQuery query = namedQuery("UPDATE Status SET status = 'finished', result = :result WHERE id = :id", Map.of("id", id, "result", result));
		DB.db.update(query);
	}
}
