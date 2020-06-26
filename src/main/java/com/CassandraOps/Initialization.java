package com.CassandraOps;

import java.util.Date;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.LocalDate;
import com.datastax.driver.core.Session;

public class Initialization {

	public static void main(String[] args) {

		Cluster cluster = Cluster.builder().addContactPoint("127.0.0.1").build();

		Date date = new java.util.Date();
		LocalDate localdate = LocalDate.fromMillisSinceEpoch(date.getTime());
		// Creating Session object
		Session session = cluster.connect();

		String query = "CREATE KEYSPACE warranty_analysis WITH replication "
				+ "= {'class':'SimpleStrategy', 'replication_factor':1};";

		// session.execute(query);

		String query2 = "CREATE TABLE warranty_analysis.reg_users( userid text PRIMARY KEY," + "lastlogin timestamp, "
				+ "password text," + "username text," + "usertype text)";
		// session.execute(query2);

		String query3 = "Insert into warranty_analysis.reg_users (userid, lastlogin, password, username"
				+ ", usertype) values ('admin', '" + localdate + "', 'admin', 'admin', 'admin')";
		session.execute(query3);

		String query4 = "Insert into warranty_analysis.reg_users (userid, lastlogin, password, username"
				+ ", usertype) values ('testuser', '" + localdate + "', 'testuser', 'TestUser', 'user')";
		session.execute(query4);

		// session.execute(query3);
		// using the KeySpace
		// session.execute("DROP table warranty_analysis.reg_users");

		System.out.println("DROP table warranty_analysis.reg_users");
		session.close();

	}

}
