package br.com.http.monitoring;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class JobMonitoringResponseTest {

	@Test
	public void testIsAllJobsHealthyAndErrorCauses() throws Exception {
		List<JobInfo> jobs = new ArrayList<>();
		List<JobExecutionInfo> history = new ArrayList<>();
		Date now = new Date();
		history.add(new JobExecutionInfo(1l, now, now, 200, "Success", null));
		String cronExpression = "0 30 * ? * * *";
		jobs.add(new JobInfo(1l, history, cronExpression));
		JobMonitoringResponse happyResponse = new JobMonitoringResponse(jobs, 1);
		assertTrue(happyResponse.areAllJobsHealthy());
		assertTrue(happyResponse.getMessages().isEmpty());

		history.add(new JobExecutionInfo(2l, now, now, 500, "Success",null ));
		jobs.add(new JobInfo(2l, history, cronExpression));
		JobMonitoringResponse sadResponse = new JobMonitoringResponse(jobs, 2);
		assertFalse(sadResponse.areAllJobsHealthy());
		assertEquals(1, sadResponse.getMessages().size());
		assertEquals("Job id 2 is not healthy", sadResponse.getMessages().get(0));

		history.add(new JobExecutionInfo(3l, now, now, 200, "BlaDifferentFromSuccess",null ));
		jobs.add(new JobInfo(3l, history, cronExpression));
		JobMonitoringResponse sadderResponse = new JobMonitoringResponse(jobs, 4);
		assertFalse(sadderResponse.areAllJobsHealthy());
		assertEquals(3, sadderResponse.getMessages().size());
		assertEquals("The number of actual active jobs is different from the expected.", sadderResponse.getMessages().get(0));
		assertEquals("Job id 2 is not healthy", sadderResponse.getMessages().get(1));
		assertEquals("Job id 3 is not healthy", sadderResponse.getMessages().get(2));

	}

}