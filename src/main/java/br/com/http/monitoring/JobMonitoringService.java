package br.com.http.monitoring;

import javax.enterprise.context.SessionScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/monitor")
@SessionScoped
public class JobMonitoringService {

	@GET
	public Response getJobInformation(@QueryParam(value = "numberOfJobs") Integer numberOfJobs,
			@QueryParam(value = "lookBackHours") Integer lookBackHours) {
		return Response.ok().build();
	}

}
