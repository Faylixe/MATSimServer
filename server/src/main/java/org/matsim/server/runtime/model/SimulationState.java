package org.matsim.server.runtime.model;

import java.time.Duration;
import java.time.Instant;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author fv
 */
@XmlRootElement(name="state")
public final class SimulationState {

	/** **/
	public static final SimulationState EMPTY = new SimulationState();

	/** Start time of the current simulation. **/
	private final Instant start;

	/** Current simulation phase. **/
	private SimulationPhase phase;

	/** Current iteration running. **/
	private Integer iteration;
	
	/**
	 * 
	 */
	public SimulationState() {
		this.phase = null;
		this.iteration = null;
		this.start = Instant.now();
	}
	
	/**
	 * 
	 * @return
	 */
	@XmlElement(name="duration")
	public String getDuration() {
		final Duration duration = Duration.between(start, Instant.now());
		return duration.toString();
	}

	/**
	 * 
	 * @return
	 */
	@XmlElement(name="iteration", required=false)
	public Integer getIteration() {
		return iteration;
	}

	/**
	 * 
	 * @param iteration
	 */
	public void incrementIteration() {
		iteration++;
	}
	
	/**
	 * 
	 */
	public void deleteIteration() {
		iteration = null;
	}
	
	/**
	 * 
	 */
	public void createIteration() {
		iteration = 0;
	}

	/**
	 * 
	 * @return
	 */
	@XmlElement(name="phase", required=false)
	public SimulationPhase getPhase() {
		return phase;
	}

	/**
	 * 
	 * @param phase
	 */
	public void setPhase(final SimulationPhase phase) {
		this.phase = phase;
	}
	
}
