package br.com.http.monitoring;

import br.com.http.utils.CronExpressionParser;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JobInfo {

	private Long jobId;
	private boolean healthy;
	private List<String> reasons;
	private String cronExpression;
	private List<JobExecutionInfo> history;

	public JobInfo(Long jobId, List<JobExecutionInfo> history, String cronExpression) {
		this.jobId = jobId;
		this.history = history;
		this.cronExpression = cronExpression;
		this.reasons = new ArrayList<>();
		updateHealthInfo();
	}

	private void updateHealthInfo() {
		if (emptyHistory()) return;
		if (healthCheckNotOk(history.get(0))) {
			healthy = false;
			return;
		}
		healthy = true;
	}

	private boolean healthCheckNotOk(JobExecutionInfo info) {
		return jobsStatusIsNotSuccessful(info) || httpStatusIsNotOK(info) || isExecutionLate(info);
	}

	private boolean emptyHistory() {
		if (history.isEmpty()) {
			healthy = false;
			reasons.add(String.format("Empty execution history for job id %d", jobId));
			return true;
		}
		return false;
	}

	protected boolean isExecutionLate(JobExecutionInfo info) {
		if (info.getStatus().equals("Running")) {
			return false;
		}
		try {
			final Date nextExecution = CronExpressionParser.nextExecution(cronExpression, info.getTsFinish());
			final Date now = new Date();
			final boolean check = nextExecution.compareTo(now) < 0;
			if(check) {
				reasons.add(String.format("Most recent execution of job id %d is late. Next execution begining on %s should be %s, and now is %s. Cron expressions: '%s'",
						jobId, info.getTsFinish(), nextExecution, now, cronExpression));
			}
			return check;
		} catch (ParseException e) {
			reasons.add(String.format("Could not parse cron expression '%s' for job id %d", cronExpression, jobId));
		}
		// if there's a parse error, assume job is not running ok
		return false;
	}

	private boolean httpStatusIsNotOK(JobExecutionInfo info) {
		if (info.getHttpResponseStatus() == null && info.getStatus().equals("Running")) {
			return false;
		}
		final boolean check = info.getHttpResponseStatus() != 200;
		if (check) {
			reasons.add(String.format("Http response code is not 200 for last execution in history"));
		}
		return check;
	}

	private boolean jobsStatusIsNotSuccessful(JobExecutionInfo info) {
		if (info.getStatus().equals("Running")) {
			return false;
		}
		final boolean check = !info.getStatus().equals("Success");
		if (check) {
			reasons.add(String.format("Http status is not Success for last execution in history"));
		}
		return check;
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
