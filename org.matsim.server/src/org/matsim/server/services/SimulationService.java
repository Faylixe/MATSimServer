package org.matsim.server.services;

import java.util.Optional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.matsim.server.runtime.MATSimRuntime;
import org.matsim.server.runtime.Simulation;
import org.matsim.server.runtime.SimulationState;

/**
 * 
 * @author fv
 */
@Path("/simulation")
public final class SimulationService {
	
	@GET
	@Path("/state")
	@Produces(MediaType.APPLICATION_XML)
	public SimulationState getState() {
		final Optional<Simulation> simulation = MATSimRuntime.getInstance().getSimulation();
		return (simulation.isPresent() ? simulation.get().getState() : SimulationState.EMPTY);
	}

}
