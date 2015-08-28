package org.matsim.server.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import org.matsim.server.common.ResponseFactory;
import org.matsim.server.exception.IllegalSimulationArchive;
import org.matsim.server.exception.SimulationNotFoundException;
import org.matsim.server.exception.SimulationOutputNotFoundException;
import org.matsim.server.runtime.MATSimRuntime;
import org.matsim.server.runtime.Simulation;
import org.matsim.server.runtime.SimulationState;
import org.matsim.server.runtime.SimulationWorkbench;

/**
 * Web service implementations for controlling simulation
 * over the server.
 * 
 * @author fv
 */
@Path("/simulation")
public final class SimulationService {
	
	/** Header key for content disposition. **/
	private static final String CONTENT = "Content-Disposition";

	/** Filename exposed into HTTP header when downloading output. **/
	private static final String FILENAME = "attachment; filename=output.zip";

	/** Workbench instance this service is working on. **/
	private final SimulationWorkbench workbench;

	/**
	 * Default constructor.
	 */
	public SimulationService() {
		this.workbench = SimulationWorkbench.getInstance();
	}

	/**
	 * Retrieves list of all running simulations available
	 * on this server instance.
	 * 
	 * @return Custom response which contains list of active id.
	 */
	@GET
	@Path("/actives")
	@Produces(MediaType.APPLICATION_XML)
	public Response getActives() {
		return ResponseFactory.createActivesEntity(workbench.getActiveSimulation());
	}

	/**
	 * Retrieves state of the simulation corresponding to the given <tt>id</tt>.
	 * Such state are defined by the following attribute :<br>
	 * 
	 * <ul>
	 * 	<li>Simulation duration</li>
	 * 	<li>(optional) current iteration if running</li>
	 * 	<li>(optional) current phase if running</li>
	 * </ul>
	 * 
	 * @param id Identifier of the simulation to retrieve state from.
	 * @return State of the simulation required.
	 * @throws SimulationNotFoundException If the given <tt>id</tt> is not a valid one.
	 */
	@GET
	@Path("/{id}/state")
	@Produces(MediaType.APPLICATION_XML)
	public SimulationState getState(@PathParam("id") final int id) {
		final Optional<SimulationState> state = workbench.getSimulationState(id);
		if (!state.isPresent()) {
			throw new SimulationNotFoundException(id);
		}
		return state.get();
	}

	/**
	 * Simulation submission. A ZIP archive file is expected
	 * containing required initial demand for running simulation.
	 * Once data are validated, simulation is started in a distinct
	 * thread and could be monitored using {@link #getState(int)}.
	 * 
	 * @param stream Input stream of the uploaded file through the POST request.
	 * @param header Header of the uploaded file through the POST request.
	 * @return Custom response which give the result of the upload.
	 */
	@POST
	@Path("/run")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_XML)
	public Response run(@FormDataParam("input") final InputStream stream, @FormDataParam("input") final FormDataContentDisposition header) {
		final Simulation simulation = workbench.createSimulation(stream);
		try {
			MATSimRuntime.commit(simulation);
		}
		catch (final Exception e) {
			throw new IllegalSimulationArchive(e);
		}
		return ResponseFactory.createCommitEntity(simulation);
	}

	/**
	 * Simulation output downloading method.
	 * It builds a new ZIP archive containing
	 * simulation output directory content in it,
	 * and returns it for downloading.
	 * 
	 * @param id Identifier of the simulation to download output from.
	 * @return Simulation output as a ZIP archive.
	 */
	@GET
	@Path("/{id}/download")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response download(@PathParam("id") final int id) {
		try {
			final Optional<FileInputStream> stream = workbench.getOutput(id);
			if (!stream.isPresent()) {
				throw new SimulationNotFoundException(id);
			}
			return Response.ok().header(CONTENT, FILENAME).entity(stream.get()).build();
		}
		catch (final FileNotFoundException e) {
			// TODO : Consider sending more specific message (still running, encounter error, or system error).
			throw new SimulationOutputNotFoundException(id);
		}
	}

}
