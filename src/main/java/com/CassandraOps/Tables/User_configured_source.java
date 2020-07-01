package com.CassandraOps.Tables;

import java.util.Map;
import java.util.UUID;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

public class User_configured_source {

	@PrimaryKeyColumn(name = "sourceid", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private String userid;
	private UUID sourceid;
	private String sourcename;
	private Map<String, String> configuration;

	private String sourcetype;

	private String type;

	public User_configured_source(String userid, UUID sourceid, String sourcename, Map<String, String> configuration,
			String sourcetype, String type) {
		super();
		this.userid = userid;
		this.sourceid = sourceid;
		this.sourcename = sourcename;
		this.configuration = configuration;
		this.sourcetype = sourcetype;
		this.type = type;
	}

	public String getSourcetype() {
		return sourcetype;
	}

	public void setSourcetype(String sourcetype) {
		this.sourcetype = sourcetype;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public UUID getSourceid() {
		return sourceid;
	}

	public void setSourceid(UUID sourceid) {
		this.sourceid = sourceid;
	}

	public String getSourcename() {
		return sourcename;
	}

	public void setSourcename(String sourcename) {
		this.sourcename = sourcename;
	}

	public Map<String, String> getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Map<String, String> configuration) {
		this.configuration = configuration;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
