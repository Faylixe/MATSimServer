package org.matsim.server.service;

import java.util.List;

import javax.ws.rs.core.Response;

import org.matsim.server.runtime.model.Simulation;

/**
 * Sets of static factory method for creating
 * HTTP response instance.
 * 
 * @author fv
 */
public final class SimulationServiceResponses {

	/** Symbol for opening XML tag. **/
	private static final String OPEN = "<";

	/** Symbol for closing XML tag. **/
	private static final String CLOSE = ">";
	
	/** Symbok for XML end tag. **/
	private static final String END = "/";

	/** Tag name for simulation identifier. **/
	private static final String ID = "id";

	/** Tag name for active category. **/
	private static final String ACTIVE = "active";

	/**
	 * Private constructor for avoiding instantiation.
	 */
	private SimulationServiceResponses() {
		// Do nothing.
	}

	/**
	 * Creates and returns an XML open tag
	 * for the given label.
	 * 
	 * @param tag Tag to open.
	 * @return Created open tag.
	 */
	private static String open(final String tag) {
		final StringBuilder builder = new StringBuilder();
		builder.append(OPEN);
		builder.append(tag);
		builder.append(CLOSE);
		return builder.toString();
	}

	/**
	 * Creates and returns an XML close tag
	 * for the given label.
	 * 
	 * @param tag Tag to close.
	 * @return Created close tag.
	 */
	private static String close(final String tag) {
		final StringBuilder builder = new StringBuilder();
		builder.append(OPEN);
		builder.append(END);
		builder.append(tag);
		builder.append(CLOSE);
		return builder.toString();
	}
	
	/**
	 * Creates an XML node for a simulation identifier.
	 * 
	 * @param id Identifier of the simulation to create node from.
	 * @return Created node.
	 */
	private static String createIdentifier(final int id) {
		final StringBuilder builder = new StringBuilder(open(ID));
		builder.append(id);
		builder.append(close(ID));
		return builder.toString();
	}

	/**
	 * Creates a HTTP response which contains a
	 * list of active simulation.
	 * 
	 * @return Created HTTP Response.
	 */
	public static Response createActivesEntity(final List<Integer> actives) {
		final StringBuilder builder = new StringBuilder();
		builder.append(open(ACTIVE));
		actives.forEach(id -> builder.append(createIdentifier(id)));
		builder.append(close(ACTIVE));
		return Response
				.ok()
				.entity(builder.toString())
				.build();
	}

	/**
	 * Creates HTTP response for a committing operation.
	 * 
	 * @param simulation Simulation that has been committed.
	 * @return Created HTTP Response.
	 */
	public static Response createCommitEntity(final Simulation simulation) {
		return Response
				.ok()
				.entity(createIdentifier(simulation.getId()))
				.build();
	}

}
