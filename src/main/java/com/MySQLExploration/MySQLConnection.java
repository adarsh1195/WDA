package com.MySQLExploration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;

import com.AppConstants.CassandraConstants;
import com.CassandraOps.SessionManager;
import com.CassandraOps.Tables.Reg_users;
import com.CassandraOps.Tables.User_configured_source;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

public class MySQLConnection {

	public String connID;
	public Connection conn;

	public MySQLConnection(String connID) {

		this.connID = connID;
	}

	public Connection getConnection() {

		User_configured_source source = getConnectionConfig(connID);

		Map<String, String> config = source.getConfiguration();

		try {
			Class.forName("com.mysql.jdbc.Driver");

			conn = DriverManager.getConnection(getConnectionUrl(config), config.get("user"), config.get("password"));

			return conn;

		} catch (Exception e) {

			System.out.println(e);

			return null;
		}

	}

	public String getConnectionUrl(Map<String, String> config) {

		return "jdbc:mysql://" + config.get("host") + ":" + config.get("port");

	}

	public void closeConnection() {

		try {
			if (conn.isClosed()) {

				System.out.println("Already Closed");
			}

			else {

				System.out.println("Closing");

				conn.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public boolean getConnStatus() {

		try {
			return conn.isClosed();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}

	public static User_configured_source getConnectionConfig(String id) {

		Session session = SessionManager.getSession(CassandraConstants.wda_keyspace);

		String query = "Select * from " + CassandraConstants.user_configured_source + " where sourceid =" + id
				+ " allow filtering;";

		CassandraOperations cassandraOps = new CassandraTemplate(session);

		List<User_configured_source> sources = cassandraOps.select(query, User_configured_source.class);

		return sources.get(0);

	}

	public static void main(String[] args) throws SQLException {

		String id = "9fb204a0-f9de-11e8-9767-45cda3f58446";

		MySQLConnection connConfig = new MySQLConnection(id);

		Connection conn = connConfig.getConnection();

		System.out.println(connConfig.getConnStatus());
		connConfig.closeConnection();
		System.out.println(connConfig.getConnStatus());

	}

}
