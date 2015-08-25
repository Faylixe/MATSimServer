package org.matsim.server.runtime;

import java.time.Instant;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author fv
 */
@XmlRootElement
public final class SimulationState {

	/** **/
	public static final SimulationState EMPTY = new SimulationState();

	/** **/
	private final Instant start;

	/** Current iteration running. **/
	private int iteration;
	
	/**
	 * 
	 */
	public SimulationState() {
		this.iteration = 0;
		this.start = Instant.now();
	}
	
	/**
	 * 
	 * @return
	 */
	public Instant getStart() {
		return start;
	}

	/**
	 * 
	 * @return
	 */
	public int getIteration() {
		return iteration;
	}

	/**
	 * 
	 * @param iteration
	 */
	public void setIteration(final int iteration) {
		this.iteration = iteration;
	}

}
