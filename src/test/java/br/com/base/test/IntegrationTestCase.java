package br.com.base.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.openejb.OpenEjbContainer;
import org.apache.openejb.jee.WebApp;
import org.apache.openejb.jee.jpa.unit.PersistenceUnit;

public class IntegrationTestCase {

	public static final String HTTP_LOCALHOST_4204 = "http://localhost:4204";

	protected PersistenceUnit setupPersistenceUnit() {
		PersistenceUnit unit = new PersistenceUnit("primary");
		unit.setJtaDataSource("openejb/Resource/ESBDS");

		unit.setProvider("org.hibernate.ejb.HibernatePersistence");

		unit.setProperty("hibernate.transaction.manager_lookup_class",
				"org.apache.openejb.hibernate.TransactionManagerLookup");

		unit.setProperty("hibernate.hbm2ddl.auto", "update");
		unit.setProperty("hibernate.show_sql", "false");
		return unit;
	}

	protected WebApp setupWebApp() {
		return new WebApp().contextRoot("");
	}

	@SuppressWarnings("serial")
	protected Properties setupConfigurationProperties() {
		return new Properties() {
			{
				setProperty(OpenEjbContainer.OPENEJB_EMBEDDED_REMOTABLE, "true");
				setProperty("ESBDS", "new://Resource?type=DataSource");
				setProperty("ESBDS.JdbcDriver", "org.hsqldb.jdbcDriver");
				setProperty("ESBDS.JdbcUrl", "jdbc:hsqldb:mem:.");

			}
		};
	}

	protected SimplifiedResponse post(String path, Map<String, String> formParameters) {
		List<NameValuePair> nameValuePairs = getNameValuePairsFromMap(formParameters);

		SimplifiedResponse responseToReturn = null;
		HttpClientBuilder clientBuilder = HttpClients.custom();
		try (CloseableHttpClient httpClient = clientBuilder.build()) {
			HttpPost request = new HttpPost(HTTP_LOCALHOST_4204 + path);
			HttpEntity entity = new UrlEncodedFormEntity(nameValuePairs);
			request.setEntity(entity);
			responseToReturn = getSimplifiedResponse(httpClient, request);
		} catch (IOException e) {
			new RuntimeException(e);
		}

		return responseToReturn;
	}

	private SimplifiedResponse getSimplifiedResponse(CloseableHttpClient httpClient, HttpUriRequest request) throws IOException {
		SimplifiedResponse responseToReturn;
		try (CloseableHttpResponse response = httpClient.execute(request)) {
			responseToReturn = new SimplifiedResponse(response.getStatusLine().getStatusCode(),
					EntityUtils.toString(response.getEntity()));
		}
		return responseToReturn;
	}

	private List<NameValuePair> getNameValuePairsFromMap(Map<String, String> formParameters) {
		List<NameValuePair> nameValuePairs = new ArrayList<>();
		for (Entry<String, String> entry : formParameters.entrySet()) {
			nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		return nameValuePairs;
	}

	protected SimplifiedResponse get(String path) {
		SimplifiedResponse responseToReturn = null;
		HttpClientBuilder clientBuilder = HttpClients.custom();
		try (CloseableHttpClient httpClient = clientBuilder.build()) {
			HttpGet request = new HttpGet(HTTP_LOCALHOST_4204 + path);
			responseToReturn = getSimplifiedResponse(httpClient, request);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return responseToReturn;
	}
}
