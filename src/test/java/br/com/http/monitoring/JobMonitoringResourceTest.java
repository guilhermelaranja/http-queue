package br.com.http.monitoring;

import br.com.base.test.IntegrationTestCase;
import br.com.base.test.SimplifiedResponse;
import br.com.http.timer.JobExecutor;
import br.com.http.timer.JobManager;
import br.com.http.timer.JobService;
import org.apache.openejb.jee.WebApp;
import org.apache.openejb.jee.jpa.unit.PersistenceUnit;
import org.apache.openejb.junit.ApplicationComposer;
import org.apache.openejb.testing.Classes;
import org.apache.openejb.testing.Configuration;
import org.apache.openejb.testing.EnableServices;
import org.apache.openejb.testing.Module;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@EnableServices(value = "jaxrs", httpDebug = true)
@RunWith(ApplicationComposer.class)
public class JobMonitoringResourceTest extends IntegrationTestCase {

	@Module
	@Classes(value = { JobMonitoringResource.class, JobMonitoringService.class, JobService.class, JobManager.class, JobExecutor.class }, cdi = true)
	public WebApp app() {
		return setupWebApp();
	}

	@Module
	public PersistenceUnit persistence() {
		return setupPersistenceUnit();
	}

	@Configuration
	public Properties configuration() {
		return setupConfigurationProperties();
	}

	@Before
	public void setup() {
		post("/job", new HashMap<String, String>() {
			{
				put("id", "3");
				put("method", "GET");
				put("url", "http://www.dextra.com.br/");
				put("cron", "* 30 * * * * *");
			}
		});

		post("/job", new HashMap<String, String>() {
			{
				put("id", "4");
				put("method", "GET");
				put("url", "http://www.google.com.br/");
				put("cron", "* 30 * * * * *");
			}
		});
	}

	@Test
	public void getNoParams() {
		SimplifiedResponse response = get("/monitor");
		assertEquals(206, response.getStatusCode());
		assertNotNull(response.getContent());
	}

	@Test
	public void getWithParams() {
		SimplifiedResponse response = get("/monitor?numberOfJobs=2&lookBackHours=2");
		assertEquals(206, response.getStatusCode());
		assertNotNull(response.getContent());

		SimplifiedResponse response2 = get("/monitor?numberOfJobs=3&lookBackHours=2");
		assertEquals(206, response2.getStatusCode());
		assertNotNull(response2.getContent());
	}

}