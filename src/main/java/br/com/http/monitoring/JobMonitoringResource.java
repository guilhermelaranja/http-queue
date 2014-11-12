package br.com.http.monitoring;

import com.google.gson.Gson;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/monitor")
@RequestScoped
public class JobMonitoringResource {

	@Inject
	private JobMonitoringService jobMonitoringService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getJobInformation(@QueryParam(value = "numberOfJobs") Integer numberOfJobs,
			@QueryParam(value = "historySize") Integer historySize) {
		int hstSize = historySize == null ? 10 : historySize;
		int numberOfJbs = (numberOfJobs == null || numberOfJobs == 0) ? -1 : numberOfJobs;
		JobMonitoringResponse responseEntity = jobMonitoringService.findJobs(numberOfJbs, hstSize);
		final String jsonResponse = new Gson().toJson(responseEntity);
		Response.Status responseStatus;
		if (responseEntity.areAllJobsHealthy() && responseEntity.getMessages().size() == 0) {
			responseStatus = Response.Status.OK;
		} else {
			responseStatus = Response.Status.PARTIAL_CONTENT;
		}
		return Response.status(responseStatus).entity(jsonResponse).build();
	}

}
