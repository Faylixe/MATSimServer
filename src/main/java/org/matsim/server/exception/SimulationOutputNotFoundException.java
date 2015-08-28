package org.matsim.server.exception;

import javax.ws.rs.WebApplicationException;

import org.matsim.server.common.ResponseFactory;

/**
 * {@link Exception} that creates HTTP response
 * for a simulation output not found.
 * 
 * @author fv
 */
public final class SimulationOutputNotFoundException extends WebApplicationException {

	/** Serialization index. **/
	private static final long serialVersionUID = 1L;

	/** Displayed error message. **/
	private static final String MESSAGE = "Output not found for simulation #%s";

	/**
	 * Default constructor.
	 * Creates associated HTTP response.
	 * 
	 * @param id Invalid simulation id caught.
	 */
	public SimulationOutputNotFoundException(final int id) {
		super(ResponseFactory.createUnknownSimulation(id, MESSAGE));
	}

}
