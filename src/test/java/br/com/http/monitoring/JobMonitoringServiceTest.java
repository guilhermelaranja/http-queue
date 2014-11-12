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
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@EnableServices(value = "jaxrs", httpDebug = true)
@RunWith(ApplicationComposer.class)
public class JobMonitoringServiceTest extends IntegrationTestCase {

	@Module
	@Classes(value = { JobMonitoringService.class }, cdi = true)
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


	@Test
	public void getNoParams() {
		SimplifiedResponse response = get("/monitor");
		assertEquals(200, response.getStatusCode());
		assertNotNull(response.getContent());
	}

}