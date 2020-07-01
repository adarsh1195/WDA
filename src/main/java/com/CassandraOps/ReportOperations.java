package com.CassandraOps;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;

import com.AppConstants.CassandraConstants;
import com.CassandraOps.Tables.User_reports;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

public class ReportOperations {

	public static JSONArray getReportForUser(String user) {

		String query = "Select * From " + CassandraConstants.user_reports + " where userid = '" + user
				+ "' ALLOW FILTERING;";

		Session session = SessionManager.getSession(CassandraConstants.wda_keyspace);

		CassandraOperations cassandraOps = new CassandraTemplate(session);

		List<User_reports> reports = cassandraOps.select(query, User_reports.class);

		JSONArray jarr = new JSONArray();

		for (int i = 0; i < reports.size(); i++) {

			JSONObject jobj = new JSONObject();

			jobj.put("Report_name", reports.get(i).getReport_name());
			jobj.put("Report_desc", reports.get(i).getReport_desc());
			jobj.put("Created", reports.get(i).getCreated());
			jobj.put("Updated", reports.get(i).getUpdated());
			jobj.put("User", reports.get(i).getUserid());
			jobj.put("Report_id", reports.get(i).getReportid());

			jarr.put(jobj);
		}

		return jarr;
	}

	public static boolean insertReport(String userid, String report_name, String report_desc) {

		Session session = SessionManager.getSession(CassandraConstants.wda_keyspace);
		UUID reportId = UUID.randomUUID();

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		StringBuilder sb = new StringBuilder("Insert into ").append(CassandraConstants.user_reports)
				.append(" (userid, reportid, report_name, report_desc,created, updated)").append(" Values (")
				.append("?,?,?,?,?,?").append(");");

		PreparedStatement prepared = session.prepare(sb.toString());

		BoundStatement bound = prepared.bind().setString("userid", userid).setUUID("reportid", reportId)
				.setString("report_name", report_name).setString("report_desc", report_desc)
				.setTimestamp("created", timestamp).setTimestamp("updated", timestamp);

		ResultSet rs = session.execute(bound);

		return rs.wasApplied();

	}

	public static void main(String[] args) {

		insertReport("admin", "Report_002", "Report of Something");
		getReportForUser("admin");

	}

}
