package org.matsim.server.runtime.model;

import java.time.Duration;
import java.time.Instant;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * State of a simulation.
 * 
 * @author fv
 */
@XmlRootElement(name="state")
public final class SimulationState {

	/** Start time of the current simulation. **/
	private final Instant start;

	/** **/
	private Duration duration;

	/** Current simulation phase. **/
	private SimulationPhase phase;

	/** Current iteration running. **/
	private Integer iteration;

	/** Optional error message displayed if an error has been notified. **/
	private String error;

	/**
	 * Default constructor.
	 */
	public SimulationState() {
		this.phase = null;
		this.error = null;
		this.iteration = null;
		this.start = Instant.now();
		this.duration = null;
	}
	
	/**
	 * Getter for duration. If simulation is not
	 * finished, current elapsed time is returned.
	 * 
	 * @return Simulation duration, or elapsed time if not finished.
	 */
	@XmlElement(name="duration", required=false)
	public String getDuration() {
		if (duration == null) {
			final Duration elapsed = Duration.between(start, Instant.now());
			return elapsed.toString();
		}
		return duration.toString();
	}

	/**
	 * Getter for error message.
	 * 
	 * @return Error message if any.
	 */
	@XmlElement(name="error", required=false)
	public String getError() {
		return error;
	}

	/**
	 * Sets the error message caught.
	 * 
	 * @param error Error message caught.
	 */
	public void setError(final String error) {
		this.error = error;
	}

	/**
	 * Getter for iteration count.
	 * 
	 * @return Number of iteration performed.
	 */
	@XmlElement(name="iteration", required=false)
	public Integer getIteration() {
		return iteration;
	}

	/**
	 * Increments iteration count.
	 */
	public void incrementIteration() {
		iteration++;
	}
	
	/**
	 * Removes iteration count (simulation is finished).
	 */
	public void deleteIteration() {
		iteration = null;
	}
	
	/**
	 * Initializes iteration count.
	 */
	public void createIteration() {
		iteration = 0;
	}

	/**
	 * Getter for the current simulation phase.
	 * 
	 * @return Current simulation phase.
	 */
	@XmlElement(name="phase", required=false)
	public SimulationPhase getPhase() {
		return phase;
	}

	/**
	 * Sets the current phase.
	 * 
	 * @param phase Current simulation phase.
	 */
	public void setPhase(final SimulationPhase phase) {
		this.phase = phase;
	}
	
}
