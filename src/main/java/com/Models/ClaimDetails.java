package com.Models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONObject;

import com.MySQLExploration.MySQLConnection;

public class ClaimDetails {

	public static JSONObject getDetails() {

		String id = "9fb204a0-f9de-11e8-9767-45cda3f58446";

		MySQLConnection connConfig = new MySQLConnection(id);

		Connection conn = connConfig.getConnection();

		JSONObject jobj = new JSONObject();

		jobj.put("Claim types", getWarrantyDetails(conn));

		jobj.put("Cause", getClaimCategory(conn));

		connConfig.closeConnection();
		return jobj;

	}

	public static JSONArray getWarrantyDetails(Connection conn) {

		try {
			Statement stmt = conn.createStatement();

			String query = "Select x.*, y.extended_warranty from \r\n"
					+ "(SELECT category, count(*) as warranty FROM bike_claims  where claimed_month between 1 and 12 group by category) x\r\n"
					+ "left join \r\n"
					+ "(select category, count(*) as extended_warranty from bike_claims where claimed_month between 13 and 18 group by category) y\r\n"
					+ "on x.category = y.category";

			ResultSet rs = stmt.executeQuery(query);

			JSONArray obj = new JSONArray();

			while (rs.next()) {

				JSONObject jobj = new JSONObject();

				String category = rs.getString(1);
				int warranty = rs.getInt(2);
				int extd_warranty = rs.getInt(3);

				jobj.put("Category", category);

				jobj.put("Warranty", warranty);

				jobj.put("Extended Warranty", extd_warranty);

				obj.put(jobj);

			}

			return obj;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	public static JSONObject getClaimCategory(Connection conn) {
		try {
			Statement stmt = conn.createStatement();

			String query = "Select category, claim_category, count(*) from bike_claims group by category, claim_category;";

			ResultSet rs = stmt.executeQuery(query);

			JSONObject obj = new JSONObject();

			while (rs.next()) {

				String category = rs.getString(1);

				String claim_cat = rs.getString(2);

				int count = rs.getInt(3);

				if (obj.has(category)) {

					obj.getJSONObject(category).put(claim_cat, count);

				} else {

					obj.put(category, new JSONObject().put(claim_cat, count));
				}

			}

			return obj;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
