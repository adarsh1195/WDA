/**
 * @author Adarsh
 *
 * 
 */

package com.CassandraOps.Tables;

import java.util.Date;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

public class Reg_users {

	@PrimaryKeyColumn(name = "userid", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private String userid;
	private Date lastlogin;
	private String password;
	private String username;
	private String usertype;

	public Reg_users(String userid, Date lastlogin, String password, String username, String usertype) {

		super();
		this.userid = userid;
		this.lastlogin = lastlogin;
		this.password = password;
		this.username = username;
		this.usertype = usertype;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public Date getLastlogin() {
		return lastlogin;
	}

	public void setLastlogin(Date lastlogin) {
		this.lastlogin = lastlogin;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsertype() {
		return usertype;
	}

	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}

}
