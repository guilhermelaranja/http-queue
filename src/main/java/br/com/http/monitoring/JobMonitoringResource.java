package br.com.http.monitoring;

import br.com.http.utils.AuthUtil;
import com.google.gson.Gson;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/monitor")
@RequestScoped
public class JobMonitoringResource {

	@Inject
	private JobMonitoringService jobMonitoringService;

	@GET
	@Path("/{authHash}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getJobInformation(@QueryParam(value = "numberOfJobs") Integer numberOfJobs,
			@QueryParam(value = "historySize") Integer historySize, @PathParam("authHash") String authHash) {

		try {
			if (authHash == null || !authHash.equals(AuthUtil.getAuthHash())) {
				return Response.status(Response.Status.NOT_FOUND).build();
			}
		} catch (IOException e) {
			return Response.serverError().build();
		}

		int hstSize = (historySize == null || historySize <= 0) ? 10 : historySize;
		int numberOfJbs = (numberOfJobs == null || numberOfJobs == 0) ? -1 : numberOfJobs;
		JobMonitoringResponse responseEntity = jobMonitoringService.findJobs(numberOfJbs, hstSize);
		final String jsonResponse = new Gson().toJson(responseEntity);
		return Response.status(Response.Status.OK).entity(jsonResponse).build();
	}

}
