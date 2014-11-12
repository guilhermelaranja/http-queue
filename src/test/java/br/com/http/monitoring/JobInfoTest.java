package br.com.http.monitoring;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JobInfoTest {

	@Test
	public void testIsHealthy() throws Exception {

		List<JobExecutionInfo> historyJob1 = new ArrayList<>();
		final Date now = new Date();
		historyJob1.add(new JobExecutionInfo(1l, now, now, 200, "Success", null));
		final String defaultCronExpression = "0 30 * ? * * *";
		JobInfo info1 = new JobInfo(1l, historyJob1, defaultCronExpression);
		assertTrue(info1.isHealthy());

		List<JobExecutionInfo> historyJob2 = new ArrayList<>(historyJob1);
		historyJob2.add(new JobExecutionInfo(2l, now, now, 500, "Success", null));
		JobInfo info2 = new JobInfo(2l, historyJob2, defaultCronExpression);
		assertFalse(info2.isHealthy());

		List<JobExecutionInfo> historyJob3 = new ArrayList<>(historyJob1);
		historyJob3.add(new JobExecutionInfo(3l, now, now, null, "Failed",
				"java.net.UnknownHostException: dev-frontend: unknown error"));
		JobInfo info3 = new JobInfo(3l, historyJob3, defaultCronExpression);
		assertFalse(info3.isHealthy());

		List<JobExecutionInfo> historyJob4 = new ArrayList<>(historyJob1);
		//ultima execucao atrasada
		final Date late = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2014");
		historyJob4.add(new JobExecutionInfo(4l, late, late, 200, "Success", null));
		JobInfo info4 = new JobInfo(3l, historyJob4, defaultCronExpression);
		assertFalse(info4.isHealthy());

	}

	@Test
	public void testExecutionIsLate() throws ParseException {
		List<JobExecutionInfo> historyJob1 = new ArrayList<>();
		final Date now = new Date();
		historyJob1.add(new JobExecutionInfo(1l, now, now, 200, "Success", null));
		final String defaultCronExpression = "0 30 * ? * * *";
		JobInfo info1 = new JobInfo(1l, historyJob1, defaultCronExpression);
		assertFalse(info1.isExecutionLate(historyJob1.get(0)));

		List<JobExecutionInfo> historyJob2 = new ArrayList<>();
		final Date late = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2014");
		historyJob2.add(new JobExecutionInfo(1l, late, late, 200, "Success", null));
		JobInfo info2 = new JobInfo(1l, historyJob2, defaultCronExpression);
		assertTrue(info2.isExecutionLate(historyJob2.get(0)));
	}
}