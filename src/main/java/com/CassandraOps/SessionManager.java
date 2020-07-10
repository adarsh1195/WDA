package com.CassandraOps;

import java.util.List;

import org.apache.log4j.Logger;

import com.AppConstants.CassandraConstants;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public class SessionManager {

	private static Cluster cluster = null;
	private static Session session = null;

	final static Logger logger = Logger.getLogger(SessionManager.class);

	public static Session getSession(String keyspace) {

		if (cluster == null) {

			createCluster();

			if (session == null) {

				session = cluster.connect(keyspace);
			} else {

				session.close();

				session = cluster.connect(keyspace);

			}

		} else {

			if (session == null) {

				session = cluster.connect(keyspace);
			} else {

				session.close();
				session = cluster.connect(keyspace);
			}
		}

		return session;
	}

	private static void createCluster() {

		cluster = Cluster.builder().addContactPoints(CassandraConstants.cassandra_host).build();
		//cluster = Cluster.builder().addContactPoint("142.102.27.102").withPort(9042).build();

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		SessionManager sm = new SessionManager();
		Session s = sm.getSession("user_profile");
		System.out.println(s.getLoggedKeyspace());
		String query = "select * from user_source";
		ResultSet rs = s.execute(query);
		// System.out.println(rs.all().size());
		List<Row> j = rs.all();
		System.out.println(j.size());
		for (int i = 0; i < j.size(); i++) {
			System.out.println(i);
			System.out.println(j.get(i));
		}

		s.close();
	}

}
