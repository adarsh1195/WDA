/**
 * @author Adarsh
 *
 * 
 */

package com.UserManagement;

import java.util.List;

import org.json.JSONObject;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;

import com.AppConstants.CassandraConstants;
import com.CassandraOps.SessionManager;
import com.CassandraOps.Tables.Reg_users;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select.Where;

public class LoginValidator {

	public static String success = "Login succesfull";
	public static String failure = "Login failed";
	public static String noUser = "User does not exist";

	public JSONObject validateUser(String user, String password) {

		JSONObject jobj = new JSONObject();

		Where select = QueryBuilder.select().all().from(CassandraConstants.reg_users)
				.where(QueryBuilder.eq("userid", user));

		Session session = SessionManager.getSession(CassandraConstants.wda_keyspace);

		CassandraOperations cassandraOps = new CassandraTemplate(session);

		List<Reg_users> regUsr = cassandraOps.select(select, Reg_users.class);

		if (regUsr.size() == 0) {

			System.out.println("User does not exist");

			jobj.put("Message", noUser);
		} else {

			if (regUsr.get(0).getPassword().equals(password)) {

				System.out.println("Login Successfull");
				jobj.put("Message", success);
				jobj.put("UserName", regUsr.get(0).getUsername());
				jobj.put("UserType", regUsr.get(0).getUsertype());
				jobj.put("LastLogin", regUsr.get(0).getLastlogin());

			}

			else {

				System.out.println("Login Failed");
				jobj.put("Message", failure);
			}

		}

		// { "" }

		return jobj;
	}

	public static void main(String[] args) {

		LoginValidator lv = new LoginValidator();

		lv.validateUser("admin", "admin");

	}
}
