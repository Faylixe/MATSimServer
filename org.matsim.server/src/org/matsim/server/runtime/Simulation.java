package org.matsim.server.runtime;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * @author fv
 */
public final class Simulation {

	/** **/
	private static final AtomicInteger ID_FACTORY = new AtomicInteger();

	/** **/
	private final int id;

	/** **/
	private final SimulationState state;

	/**
	 * 
	 */
	public Simulation() {
		this.id = ID_FACTORY.getAndIncrement();
		this.state = new SimulationState();
	}

	/**
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * 
	 * @return
	 */
	public SimulationState getState() {
		return state;
	}

}
