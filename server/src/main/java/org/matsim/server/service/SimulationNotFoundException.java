package org.matsim.server.service;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * 
 * @author fv
 */
public final class SimulationNotFoundException extends WebApplicationException {

	/** Serialization index. **/
	private static final long serialVersionUID = 1L;

	/** **/
	private static final int STATUS = 404;

	/** **/
	private static final String TYPE = "application/xml";

	/** **/
	private static final String MESSAGE = "<?xml version=\"1.0\" ?>\n<error>Simulation %s not found</error>";

	/**
	 * 
	 * @param id
	 * @return
	 */
	private static Response createResponse(final int id) {
		return Response
				.status(STATUS)
				.entity(String.format(MESSAGE, id))
				.type(TYPE)
				.build();
	}

	/**
	 * 
	 * @param id
	 */
	public SimulationNotFoundException(final int id) {
		super(createResponse(id));
	}
	
}
