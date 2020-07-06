package com.Models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONObject;

import com.MySQLExploration.MySQLConnection;

public class SalesReport {

	public static JSONObject getSalesReport() {

		String id = "9fb204a0-f9de-11e8-9767-45cda3f58446";

		MySQLConnection connConfig = new MySQLConnection(id);

		Connection conn = connConfig.getConnection();

		JSONObject jobj = new JSONObject();

		jobj.put("Sales by Category", getSalesReportCategory(conn));

		jobj.put("Sales by Model", getSalesReportModel(conn));

		jobj.put("Revenue", getRevenue(conn));

		connConfig.closeConnection();

		return jobj;
	}

	public static JSONObject getSalesReportCategory(Connection conn) {

		try {
			Statement stmt = conn.createStatement();

			String query = "select year(joined.order_date), joined.category2, sum(joined.quantity) from "
					+ "(select * from orders left join bikes on orders.product_id = bikes.bike_id) joined "
					+ "group by  joined.category2, year(joined.order_date);";

			ResultSet rs = stmt.executeQuery(query);

			JSONObject obj = new JSONObject();

			while (rs.next()) {

				int year = rs.getInt(1);
				String category = rs.getString(2);
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

	public static JSONObject getSalesReportModel(Connection conn) {
		try {
			Statement stmt = conn.createStatement();

			String query = "select  joined.category2,year(joined.order_date), joined.model, sum(joined.quantity) from "
					+ "(select * from orders left join bikes on orders.product_id = bikes.bike_id) joined "
					+ "group by  joined.category2, year(joined.order_date), joined.model;";

			ResultSet rs = stmt.executeQuery(query);

			JSONObject obj = new JSONObject();

			while (rs.next()) {
				String category = rs.getString(1);
				int year = rs.getInt(2);
				String model = rs.getString(3);
				int quant = rs.getInt(4);

				if (obj.has(category)) {

					if (obj.getJSONObject(category).has(model)) {

						obj.getJSONObject(category).getJSONObject(model).put(Integer.toString(year), quant);
					}

					else {

						obj.getJSONObject(category).put(model, new JSONObject().put(Integer.toString(year), quant));
					}

				}

				else {

					obj.put(category, new JSONObject().put(model, new JSONObject().put(Integer.toString(year), quant)));

				}

			}

			return obj;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static JSONObject getRevenue(Connection conn) {

		try {
			Statement stmt = conn.createStatement();

			String query = "Select *, a.amount/a.quantity from ("
					+ "				select  joined.category2, sum(joined.price) as amount, sum(joined.quantity) as quantity from"
					+ "				(select * from orders left join bikes on orders.product_id = bikes.bike_id) joined"
					+ "				group by  joined.category2) a;";

			ResultSet rs = stmt.executeQuery(query);

			JSONObject obj = new JSONObject();

			while (rs.next()) {

				String category = rs.getString(1);
				int revenue = rs.getInt(2);
				int quant = rs.getInt(3);
				int avg = rs.getInt(4);

				obj.put(category,
						new JSONObject().put("Revenue", revenue).put("Total Customers", quant).put("Average", avg));

			}

			return obj;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	public static void main(String[] args) {

		getSalesReport();
	}

}
