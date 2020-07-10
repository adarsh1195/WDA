package com.Models;

import java.sql.Connection;

import org.json.JSONObject;

import com.MySQLExploration.MySQLConnection;

public class ClaimsModel {

	public JSONObject getClaims() {

		String id = "9fb204a0-f9de-11e8-9767-45cda3f58446";

		MySQLConnection connConfig = new MySQLConnection(id);

		Connection conn = connConfig.getConnection();

		JSONObject jobj = new JSONObject();
		
		
		connConfig.closeConnection();

		return jobj;
	}

	public JSONObject getAvgClaimsPerCategory(Connection conn) {
		

	}

	public JSONObject claimsByGeography(Connection conn) {
		
		
		
	}
	public  JSONObject totalClaimsperCategory(Connection conn) {
		
		
	}
	
	public JSONObject claimsDetails(Connection conn) {
		
		total claims
		total claims amount
		total claims by category 
		total claims amount by category
		
		
	}
	
	public JSONObject claimCause(Connection conn) {
		
		
	}
}
