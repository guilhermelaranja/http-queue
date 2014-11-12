package br.com.http.monitoring;

import br.com.http.utils.CronExpressionParser;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class JobInfo {

	private Long jobId;
	private boolean healthy;
	private String cronExpression;
	private List<JobExecutionInfo> history;

	public JobInfo(Long jobId, List<JobExecutionInfo> history, String cronExpression) {
		this.jobId = jobId;
		this.history = history;
		this.cronExpression = cronExpression;
		updateHealthInfo();
	}

	private void updateHealthInfo() {
		for (JobExecutionInfo info : history) {
			if (jobsStatusIsNotSuccessful(info) || httpStatusIsNotOK(info) || isExecutionLate(info)) {
				healthy = false;
				return;
			}
		}
		healthy = true;
	}

	protected boolean isExecutionLate(JobExecutionInfo info) {
		try {
			return CronExpressionParser.nextExecution(cronExpression,info.getTsFinish()).compareTo(new Date()) < 0;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// if there's a parse error, assume job is not running ok
		return false;
	}

	private boolean httpStatusIsNotOK(JobExecutionInfo info) {
		return info.getHttpResponseStatus() != 200;
	}

	private boolean jobsStatusIsNotSuccessful(JobExecutionInfo info) {
		return !info.getStatus().equals("Success");
	}

	public Long getJobId() {
		return jobId;
	}

	public boolean isHealthy() {
		return healthy;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public List<JobExecutionInfo> getHistory() {
		return history;
	}
}
