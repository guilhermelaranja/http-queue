package br.com.http.queue;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class HttpRequestMessageTest {

	private boolean result;
	private int statusCode;

	public HttpRequestMessageTest(boolean result, int statusCode) {
		this.result = result;
		this.statusCode = statusCode;
	}

	@Test
	public void testSuccessful() {
		HttpRequestMessage httpRequestMessage = new HttpRequestMessage("GET", "www.google.com");
		httpRequestMessage.setResponseStatus(statusCode);
		assertEquals(result, httpRequestMessage.success());
	}

	@SuppressWarnings("rawtypes")
	@Parameterized.Parameters
	public static Collection parameters() {
		return Arrays.asList(new Object[][] { { true, 200 }, { true, 201 }, { true, 299 }, { false, 300 },
				{ false, 400 }, { false, 400 } });
	}

}
