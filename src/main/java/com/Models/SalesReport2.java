package com.Models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import com.MySQLExploration.MySQLConnection;

public class SalesReport2 {

	public static JSONObject getSalesReport() {

		String id = "9fb204a0-f9de-11e8-9767-45cda3f58446";

		MySQLConnection connConfig = new MySQLConnection(id);

		Connection conn = connConfig.getConnection();

		JSONObject jobj = new JSONObject();

		jobj.put("Sales by Category", getSalesReportCategory(conn));

		JSONObject revenue = getRevenue(conn);

		JSONObject geography = getTotalGeographicDataByCategory(conn);

		// JSONObject salesmodel = getSalesReportModel(conn);
		JSONObject salesbymodel = getSalesRevenueModel(conn);
		Iterator<String> k = revenue.keys();
		JSONObject consolidated = new JSONObject();

		while (k.hasNext()) {

			String cat = k.next();
			consolidated.put(cat, new JSONObject().put("Revenue", revenue.getJSONObject(cat))
					.put("Geography", geography.getJSONArray(cat)).put("Models", salesbymodel.getJSONArray(cat)));

		}

		// jobj.put("Sales by Model", salesbymodel);
		// jobj.put("Revenue", revenue);
		// jobj.put("Geography by Category", geography);

		jobj.put("Geography", getTotalGeographicData(conn));
		jobj.put("Models", consolidated);

		connConfig.closeConnection();

		return jobj;
	}

	public static JSONObject getSalesRevenueModel(Connection conn) {

		try {
			Statement stmt = conn.createStatement();

			String query = "select  joined.category2, joined.model, sum(joined.quantity), sum(joined.price) from "
					+ "(select * from orders left join bikes on orders.product_id = bikes.bike_id) joined "
					+ "group by  joined.category2, joined.model;";

			ResultSet rs = stmt.executeQuery(query);

			JSONObject obj = new JSONObject();

			while (rs.next()) {

				String category = rs.getString(1);
				String model = rs.getString(2);
				int quant = rs.getInt(3);
				int price = rs.getInt(4);

				if (obj.has(category)) {

					obj.getJSONArray(category)
							.put(new JSONObject().put("Quantity", quant).put("Revenue", price).put("Model", model));

				} else {

					obj.put(category, new JSONArray()
							.put(new JSONObject().put("Quantity", quant).put("Revenue", price).put("Model", model)));

				}

			}

			return obj;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

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

				String year = rs.getObject(1).toString();
				String category = rs.getString(2);
				int quant = rs.getInt(3);

				if (obj.has(year)) {

					obj.getJSONObject(year).put(category, quant);
				}

				else {

					obj.put(year, new JSONObject().put(category, quant));
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

			String query = "select  joined.category2,year(joined.order_date), joined.model, sum(joined.quantity), sum(joined.price) from "
					+ "(select * from orders left join bikes on orders.product_id = bikes.bike_id) joined "
					+ "group by  joined.category2, year(joined.order_date), joined.model;";

			ResultSet rs = stmt.executeQuery(query);

			JSONObject obj = new JSONObject();

			while (rs.next()) {
				String category = rs.getString(1);
				int year = rs.getInt(2);
				String model = rs.getString(3);
				int quant = rs.getInt(4);
				int price = rs.getInt(5);

				if (obj.has(category)) {

					if (obj.getJSONObject(category).has(model)) {

						obj.getJSONObject(category).getJSONObject(model).put(Integer.toString(year),
								new JSONObject().put("Quantity", quant).put("Revenue", price));
					}

					else {

						obj.getJSONObject(category).put(model, new JSONObject().put(Integer.toString(year),
								new JSONObject().put("Quantity", quant).put("Revenue", price)));
					}

				}

				else {

					obj.put(category, new JSONObject().put(model, new JSONObject().put(Integer.toString(year),
							new JSONObject().put("Quantity", quant).put("Revenue", price))));

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

	public static JSONObject getTotalGeographicDataByCategory(Connection conn) {

		try {
			Statement stmt = conn.createStatement();

			String query = "Select joined.category2,joined.bikeshop_name, joined.bikeshop_city, joined.bikeshop_state, joined.latitude, joined.longitude, sum(joined.quantity), sum(joined.price) from (\r\n"
					+ "					Select * from bikeshops\r\n"
					+ "					left join orders on bikeshops.bikeshop_id = orders.customer_id \r\n"
					+ "					left join bikes on orders.product_id = bikes.bike_id\r\n"
					+ "					) joined\r\n"
					+ "					group by joined.category2, joined.bikeshop_name, joined.bikeshop_city, joined.bikeshop_state, joined.latitude, joined.longitude";

			ResultSet rs = stmt.executeQuery(query);

			JSONObject obj = new JSONObject();

			while (rs.next()) {

				JSONObject jobj = new JSONObject();

				jobj.put("City", rs.getObject(3));
				jobj.put("State", rs.getObject(4));
				jobj.put("Latitude", rs.getObject(5));
				jobj.put("Longitude", rs.getObject(6).toString().trim());
				jobj.put("Quantity", rs.getObject(7));
				jobj.put("Revenue", rs.getObject(8));

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

	public static JSONArray getTotalGeographicData(Connection conn) {

		try {
			Statement stmt = conn.createStatement();

			String query = "Select joined.bikeshop_name, joined.bikeshop_city, joined.bikeshop_state, joined.latitude, joined.longitude, sum(joined.quantity), sum(joined.price) from (\r\n"
					+ "Select * from bikeshops\r\n"
					+ "left join orders on bikeshops.bikeshop_id = orders.customer_id \r\n"
					+ "left join bikes on orders.product_id = bikes.bike_id\r\n" + ") joined\r\n"
					+ "group by joined.bikeshop_name, joined.bikeshop_city, joined.bikeshop_state, joined.latitude, joined.longitude;";

			ResultSet rs = stmt.executeQuery(query);

			JSONArray obj = new JSONArray();

			while (rs.next()) {

				JSONObject jobj = new JSONObject();

				jobj.put("City", rs.getObject(2));
				jobj.put("State", rs.getObject(3));
				jobj.put("Latitude", rs.getObject(4));
				jobj.put("Longitude", rs.getObject(5).toString().trim());
				jobj.put("Quantity", rs.getObject(6));
				jobj.put("Revenue", rs.getObject(7));

				obj.put(jobj);

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
