package org.matsim.server.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.matsim.server.MATSimServerConstants;

/**
 * {@link Exception} that creates HTTP response
 * for a not valid simulation archive.
 * 
 * @author fv
 */
public final class IllegalSimulationArchive extends WebApplicationException {
	
	/** Serialization index. **/
	private static final long serialVersionUID = 1L;

	/** Associated HTTP error status. **/
	private static final int STATUS = 500;

	/** Displayed error as XML tree. **/
	private static final String MESSAGE = "<error><message>Provided input archive not valid : %s</message><stack>%s</stack></error>";

	/**
	 * Static factory method that creates a valid
	 * HTTP response from a given exception.
	 * 
	 * @param e Exception to create response from.
	 * @return Created response.
	 */
	private static Response createResponse(final Exception e) {
		final String entity = String.format(MESSAGE, e.getMessage(), ExceptionUtils.getStackTrace(e));
		return Response
				.status(STATUS)
				.entity(entity)
				.type(MATSimServerConstants.ERROR_RESPONSE_TYPE)
				.build();
	}

	/**
	 * Default constructor.
	 * Creates associated HTTP response.
	 * 
	 * @param e Wrapped exception.
	 */
	public IllegalSimulationArchive(final Exception e) {
		super(createResponse(e));
	}

}
