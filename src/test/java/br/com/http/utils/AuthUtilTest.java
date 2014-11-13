package br.com.http.utils;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class AuthUtilTest {

	@Test
	public void testGetAuthKey() throws IOException {
		assertEquals("366b22c1939b35b9f4554e375e12b6bd2835458ae2fc2403fcf3755893a2754a89b7542a407ee428d40a00a54fc15d97ae6aba527bb40a125d577a5282c17111", AuthUtil.getAuthHash());
	}
}