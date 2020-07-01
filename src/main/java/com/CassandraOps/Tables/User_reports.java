package com.CassandraOps.Tables;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

public class User_reports {

	@PrimaryKeyColumn(name = "reportid", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private UUID reportid;
	private String userid;
	private String report_name;
	private String report_desc;
	private Date created;
	private Date updated;

	public User_reports(String userid, UUID reportid, String report_name, String report_desc, Date created,
			Date updated) {
		super();
		this.userid = userid;
		this.reportid = reportid;
		this.report_name = report_name;
		this.report_desc = report_desc;
		this.created = created;
		this.updated = updated;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public UUID getReportid() {
		return reportid;
	}

	public void setReportid(UUID reportid) {
		this.reportid = reportid;
	}

	public String getReport_name() {
		return report_name;
	}

	public void setReport_name(String report_name) {
		this.report_name = report_name;
	}

	public String getReport_desc() {
		return report_desc;
	}

	public void setReport_desc(String report_desc) {
		this.report_desc = report_desc;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

}
