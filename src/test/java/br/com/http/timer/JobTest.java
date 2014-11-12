package br.com.http.timer;

import org.junit.Test;

import static org.junit.Assert.*;

public class JobTest {

	@Test
	public void testGetCronExpressionSafeForQuartzParser() throws Exception {
		Job job = new Job(1l);
		assertEquals("* * * ? * * *", job.getCronExpressionSafeForQuartzParser());

		job.setDayOfMonth("10");
		assertEquals("* * * 10 * ? *", job.getCronExpressionSafeForQuartzParser());

		job.setDayOfWeek("5");
		assertEquals("* * * 10 * ? *", job.getCronExpressionSafeForQuartzParser());

		Job job2 = new Job(2l);
		job2.setDayOfWeek("5");
		assertEquals("* * * ? * 5 *", job2.getCronExpressionSafeForQuartzParser());

		job2.setDayOfMonth("10");
		assertEquals("* * * 10 * ? *", job2.getCronExpressionSafeForQuartzParser());
	}
}