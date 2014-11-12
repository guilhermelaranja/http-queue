package br.com.http.monitoring;

import java.util.Date;

public class JobExecutionInfo {

	private Long jobExecutionId;
	private Date tsFinish;
	private Date tsStart;
	private int httpResponseStatus;
	private String status;

	public JobExecutionInfo(Long jobExecutionId, Date tsFinish, Date tsStart, int httpResponseStatus, String status) {
		this.jobExecutionId = jobExecutionId;
		this.tsFinish = tsFinish;
		this.tsStart = tsStart;
		this.httpResponseStatus = httpResponseStatus;
		this.status = status;
	}

	public Long getJobExecutionId() {
		return jobExecutionId;
	}

	public Date getTsFinish() {
		return tsFinish;
	}

	public int getHttpResponseStatus() {
		return httpResponseStatus;
	}

	public String getStatus() {
		return status;
	}

	public Date getTsStart() {
		return tsStart;
	}
}
