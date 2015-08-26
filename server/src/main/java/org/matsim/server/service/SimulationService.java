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
import org.matsim.server.runtime.SimulationWorkbench;
import org.matsim.server.runtime.model.Simulation;
import org.matsim.server.runtime.model.SimulationState;

/**
 * 
 * @author fv
 */
@Path("/simulation")
public final class SimulationService {

	/** **/
	private final SimulationWorkbench runtime;

	/**
	 * 
	 */
	public SimulationService() {
		this.runtime = SimulationWorkbench.getInstance();
	}

	/**
	 * 
	 * @return
	 */
	@GET
	@Path("/actives")
	@Produces(MediaType.APPLICATION_XML)
	public Response getActives() {
		final StringBuffer builder = new StringBuffer();
		builder.append("<active>");
		runtime
				.getSimulations()
				.filter(Simulation::isActive)
				.map(Simulation::getId)
				.forEach(id -> {
					builder.append("<id>").append(id).append("</id>");
				});
		builder.append("</active>");
		return Response.status(200).entity(builder.toString()).build();
	}

	/**
	 * 
	 * @return
	 */
	@GET
	@Path("/{id}/state")
	@Produces(MediaType.APPLICATION_XML)
	public SimulationState getState(@PathParam("id") final int id) {
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
		final Simulation simulation = runtime.createSimulation(stream);
		MATSimRuntime.commit(simulation);
		return Response.status(200).build();
	}

}
