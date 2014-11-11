package br.com.http.utils;

import org.quartz.CronExpression;

import java.text.ParseException;
import java.util.Date;

public class CronExpressionParser {

	public static Date nextExecution(String expr, Date from) throws ParseException {
		return new CronExpression(expr).getNextValidTimeAfter(from);
	}
}
