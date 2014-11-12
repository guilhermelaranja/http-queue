package br.com.http.monitoring;

import java.util.List;

public class JobMonitoringResponse {

	private boolean allJobsHealthy;
	private List<String> erroCauses;

	private List<JobInfo> jobs;
}
