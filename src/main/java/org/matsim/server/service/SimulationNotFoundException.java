package org.matsim.server.service;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.matsim.server.MATSimServerConstants;

/**
 * {@link Exception} that creates HTTP response
 * for a not valid simulation id.
 * 
 * @author fv
 */
public final class SimulationNotFoundException extends WebApplicationException {

	/** Serialization index. **/
	private static final long serialVersionUID = 1L;

	/** Associated HTTP error status. **/
	private static final int STATUS = 404;

	/** Displayed error as XML tree. **/
	private static final String MESSAGE = "<?xml version=\"1.0\" ?>\n<error>Simulation %s not found</error>";

	/**
	 * Static factory method that creates a valid
	 * HTTP response from a given exception.
	 * 
	 * @param id Invalid simulation id caught.
	 * @return Created response.
	 */
	private static Response createResponse(final int id) {
		return Response
				.status(STATUS)
				.entity(String.format(MESSAGE, id))
				.type(MATSimServerConstants.ERROR_RESPONSE_TYPE)
				.build();
	}

	/**
	 * Default constructor.
	 * Creates associated HTTP response.
	 * 
	 * @param id Invalid simulation id caught.
	 */
	public SimulationNotFoundException(final int id) {
		super(createResponse(id));
	}
	
}
