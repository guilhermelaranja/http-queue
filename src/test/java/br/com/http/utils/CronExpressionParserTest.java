package br.com.http.utils;

import org.junit.Test;
import org.quartz.CronExpression;

import java.text.ParseException;
import java.util.Date;

import static org.junit.Assert.*;

public class CronExpressionParserTest {

	@Test
	public void testParseOK() throws ParseException {
		String[] productionJobExpressions =
				{
						"0 0 * ? * * *",
						"0 5 * ? * * *",
						"0 25 * ? * * *",
						"0 10 * ? * * *",
						"0 0 * ? * * *",
						"0 0 2 ? * * *",
						"0 0 3 ? * * *",
						"0 30 * ? * * *",
						"0 5 2 ? * * *",
						"0 35 * ? * * *",
						"0 30 * ? * * *",
						"0 0 21 ? * * *",
						"30 */5 * ? * * *",
						"0 45 4 ? * * *",
						"0 45 5 ? * * *",
						"0 50 * ? * * *"
				};

		final Date now = new Date();
		for (String expr : productionJobExpressions) {
			// proximas execucoes sempre no futuro...
			assertTrue(now.compareTo(CronExpressionParser.nextExecution(expr, now)) < 0);
		}
	}
}