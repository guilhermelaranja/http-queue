package br.com.http.monitoring;

import java.util.ArrayList;
import java.util.List;

public class JobMonitoringResponse {

	private boolean allJobsHealthy;
	private List<String> messages;
	private List<JobInfo> jobs;

	public JobMonitoringResponse(List<JobInfo> jobs, int expectedNumberOfActiveJobs) {
		this.jobs = jobs;
		this.messages = new ArrayList<>();
		init(expectedNumberOfActiveJobs);
	}

	private void init(int expectedNumberOfActiveJobs) {
		if (expectedNumberOfActiveJobs != -1) {
			checkNumberOfJobs(expectedNumberOfActiveJobs);
		}
		allJobsHealthy = checkJobs();
	}

	private void checkNumberOfJobs(int expectedNumberOfActiveJobs) {
		if (expectedNumberOfActiveJobs != jobs.size()) {
			messages.add("The number of actual active jobs is different from the expected.");
		}
	}

	private boolean checkJobs() {
		boolean result = true;
		for(JobInfo info : jobs) {
			if (!info.isHealthy()) {
				result = false;
				messages.add(String.format("Job id %d is not healthy", info.getJobId()));
			}
		}
		return result;
	}

	public boolean areAllJobsHealthy() {
		return allJobsHealthy;
	}

	public List<String> getMessages() {
		return messages;
	}

	public List<JobInfo> getJobs() {
		return jobs;
	}
}
