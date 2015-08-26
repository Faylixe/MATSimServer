package org.matsim.server.service;

import java.io.InputStream;
import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.matsim.server.runtime.MATSimRuntime;
import org.matsim.server.runtime.Simulation;
import org.matsim.server.runtime.SimulationState;

/**
 * 
 * @author fv
 */
@Path("/simulation")
public final class SimulationService {
	
	/**
	 * 
	 * @return
	 */
	@GET
	@Path("/{id}/state")
	@Produces(MediaType.APPLICATION_XML)
	public SimulationState getState(@PathParam("id") final int id) {
		final MATSimRuntime runtime = MATSimRuntime.getInstance();
		final Optional<Simulation> simulation = runtime.getSimulation(id);
		if (!simulation.isPresent()) {
			throw new SimulationNotFoundException(id);
		}
		return simulation.get().getState();
	}

	/**
	 * 
	 * @param stream
	 * @param header
	 */
	@POST
	@Path("/run")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response run(@FormDataParam("input") final InputStream stream, @FormDataParam("input") final FormDataContentDisposition header) {
		// TODO : Write file.
		// TODO : Extract content.
		// TODO : Load simulation instance.
		// TODO : Run created simulation.
		return Response.status(200).build();
	}

}
