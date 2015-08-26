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
 * Web service implementations for controlling simulation
 * over the server.
 * 
 * @author fv
 */
@Path("/simulation")
public final class SimulationService {

	/** Workbench instance this service is working on. **/
	private final SimulationWorkbench workbench;

	/**
	 * Default constructor.
	 */
	public SimulationService() {
		this.workbench = SimulationWorkbench.getInstance();
	}

	/**
	 * RESTFull call which enumerates ids of
	 * all active simulations.
	 * <br>
	 * Associated URL : 
	 * <tt>/simulation/actives</tt>
	 * 
	 * @return Custom response which contains list of active id.
	 */
	@GET
	@Path("/actives")
	@Produces(MediaType.APPLICATION_XML)
	public Response getActives() {
		final StringBuffer builder = new StringBuffer();
		builder.append("<active>");
		workbench
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
	 * RESTFull call which retrieves state of
	 * a given simulation.
	 * <br>
	 * Associated URL : 
	 * <tt>/simulation/{id}/state</tt>
	 * 
	 * @param id Identifier of the simulation to retrieve state from.
	 * @return State of the simulation required.
	 * @throws SimulationNotFoundException If the given <tt>id</tt> is not a valid one.
	 */
	@GET
	@Path("/{id}/state")
	@Produces(MediaType.APPLICATION_XML)
	public SimulationState getState(@PathParam("id") final int id) {
		final Optional<Simulation> simulation = workbench.getSimulation(id);
		if (!simulation.isPresent()) {
			throw new SimulationNotFoundException(id);
		}
		return simulation.get().getState();
	}

	/**
	 * RESTFull call which start a new simulation
	 * from a given uploaded simulation archive
	 * which will contains our simulation input files.
	 * <br>
	 * Associated URL : 
	 * <tt>/simulation/run</tt>
	 * 
	 * @param stream Input stream of the uploaded file through the POST request.
	 * @param header Header of the uploaded file through the POST request.
	 * @return Custom response which give the result of the upload.
	 */
	@POST
	@Path("/run")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response run(@FormDataParam("input") final InputStream stream, @FormDataParam("input") final FormDataContentDisposition header) {
		final Simulation simulation = workbench.createSimulation(stream);
		MATSimRuntime.commit(simulation);
		return Response.status(200).build();
	}

}
