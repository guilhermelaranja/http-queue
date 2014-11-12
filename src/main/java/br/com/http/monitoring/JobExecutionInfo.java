package br.com.http.monitoring;

import java.util.Date;

public class JobExecutionInfo {

	private final String clientError;
	private final Long jobExecutionId;
	private final Date tsFinish;
	private final Date tsStart;
	private final Integer httpResponseStatus;
	private final String status;

	public JobExecutionInfo(Long jobExecutionId, Date tsFinish, Date tsStart, Integer httpResponseStatus, String status,
			String clientError) {
		this.jobExecutionId = jobExecutionId;
		this.tsFinish = tsFinish;
		this.tsStart = tsStart;
		this.httpResponseStatus = httpResponseStatus;
		this.status = status;
		this.clientError = clientError;
	}

	public Long getJobExecutionId() {
		return jobExecutionId;
	}

	public Date getTsFinish() {
		return tsFinish;
	}

	public Integer getHttpResponseStatus() {
		return httpResponseStatus;
	}

	public String getStatus() {
		return status;
	}

	public Date getTsStart() {
		return tsStart;
	}

	public String getClientError() {
		return clientError;
	}
}
