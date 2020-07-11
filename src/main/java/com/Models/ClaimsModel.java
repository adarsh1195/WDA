package com.Models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONObject;

import com.MySQLExploration.MySQLConnection;

public class ClaimsModel {

	public static JSONObject getClaims() {

		String id = "9fb204a0-f9de-11e8-9767-45cda3f58446";

		MySQLConnection connConfig = new MySQLConnection(id);

		Connection conn = connConfig.getConnection();

		JSONObject jobj = new JSONObject();

		jobj.put("Total claims by year", getClaimsPerCategoryByYear(conn));

		jobj.put("Total claims by category", getTotalClaims(conn));

		jobj.put("Claim amount by category", claimAmountbyCategory(conn));
		jobj.put("Geography by category", claimsByGeography(conn));
		jobj.put("Statistics",totalAmountbyCategory(conn));
		// jobj.put(key, value)

		System.out.println(jobj);

		connConfig.closeConnection();

		return jobj;
	}

	public static JSONObject getTotalClaims(Connection conn) {

		try {
			Statement stmt = conn.createStatement();

			String query = "select category,  count(*) from bike_claims group by category";

			ResultSet rs = stmt.executeQuery(query);

			JSONObject obj = new JSONObject();

			while (rs.next()) {

				String category = rs.getString(1);
				int quant = rs.getInt(2);
				obj.put(category, quant);

			}

			return obj;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	public static JSONObject getClaimsPerCategoryByYear(Connection conn) {

		try {
			Statement stmt = conn.createStatement();

			String query = "select category, year(order_date),count(*) from bike_claims group by category, year(order_date)";

			ResultSet rs = stmt.executeQuery(query);

			JSONObject obj = new JSONObject();

			while (rs.next()) {

				String category = rs.getString(1);
				int year = rs.getInt(2);
				int quant = rs.getInt(3);

				if (obj.has(category)) {

					obj.getJSONObject(category).put(Integer.toString(year), quant);
				}

				else {

					obj.put(category, new JSONObject().put(Integer.toString(year), quant));
				}

			}

			return obj;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	public static JSONObject totalAmountbyCategory(Connection conn) {

		try {
			Statement stmt = conn.createStatement();

			String query = "select category,sum(claim_amount) as amt, count(*) as cnt, sum(claim_amount)/count(*) as avrg from bike_claims group by category";

			ResultSet rs = stmt.executeQuery(query);

			JSONObject obj = new JSONObject();

			while (rs.next()) {

				String category = rs.getString(1);
				int amt = rs.getInt(2);
				int total = rs.getInt(3);
				int avg = rs.getInt(4);
				obj.put(category, new JSONObject().put("Total Claims Amount", amt).put("Claims count", total)
						.put("Average claim amount", avg));

			}

			return obj;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	public static JSONObject claimAmountbyCategory(Connection conn) {

		try {
			Statement stmt = conn.createStatement();

			String query = "select category, year(order_date),sum(claim_amount) from bike_claims group by category, year(order_date)";

			ResultSet rs = stmt.executeQuery(query);

			JSONObject obj = new JSONObject();

			while (rs.next()) {

				String category = rs.getString(1);
				int year = rs.getInt(2);
				int quant = rs.getInt(3);

				if (obj.has(category)) {

					obj.getJSONObject(category).put(Integer.toString(year), quant);
				}

				else {

					obj.put(category, new JSONObject().put(Integer.toString(year), quant));
				}

			}

			return obj;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	public static JSONObject claimsByGeography(Connection conn) {

		try {
			Statement stmt = conn.createStatement();

			String query = "Select joined.category,joined.bikeshop_name, joined.bikeshop_city, joined.bikeshop_state, joined.latitude, joined.longitude, count(*) as quantity, sum(joined.claim_amount) as claims_amount from ( \r\n"
					+ "Select * from bikeshops \r\n"
					+ "left join bike_claims on bikeshops.bikeshop_id = bike_claims.customer_id\r\n"
					+ ") joined group by joined.category,joined.bikeshop_name, joined.bikeshop_city, joined.bikeshop_state, joined.latitude, joined.longitude";

			ResultSet rs = stmt.executeQuery(query);

			JSONObject obj = new JSONObject();

			while (rs.next()) {

				JSONObject jobj = new JSONObject();

				jobj.put("City", rs.getObject(3));
				jobj.put("State", rs.getObject(4));
				jobj.put("Latitude", rs.getObject(5));
				jobj.put("Longitude", rs.getObject(6).toString().trim());
				jobj.put("Quantity", rs.getObject(7));
				jobj.put("ClaimsAmount", rs.getObject(8));

				String category = rs.getString(1);

				if (obj.has(category)) {
					obj.getJSONArray(category).put(jobj);

				} else {

					obj.put(category, new JSONArray().put(jobj));
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
		ClaimsModel.getClaims();
	}
}
