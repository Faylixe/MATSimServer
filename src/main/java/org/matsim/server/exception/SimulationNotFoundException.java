package org.matsim.server.exception;

import javax.ws.rs.WebApplicationException;

import org.matsim.server.common.ResponseFactory;

/**
 * {@link Exception} that creates HTTP response
 * for a not valid simulation id.
 * 
 * @author fv
 */
public final class SimulationNotFoundException extends WebApplicationException {

	/** Serialization index. **/
	private static final long serialVersionUID = 1L;

	/** Displayed error message. **/
	private static final String MESSAGE = "Simulation %s not found";

	/**
	 * Default constructor.
	 * Creates associated HTTP response.
	 * 
	 * @param id Invalid simulation id caught.
	 */
	public SimulationNotFoundException(final int id) {
		super(ResponseFactory.createUnknownSimulation(id, MESSAGE));
	}

}
