package org.matsim.server.runtime.model;

/**
 * Enumeration of simulation life phase.
 * 
 * @author fv
 */
public enum SimulationPhase {

	/** Simulation is stating. **/
	STARTUP,

	/** Simulation is over. **/
	FINISHED,

	/** An error occurs. **/
	ERROR,

	/** The simulation phase that runs all agents's scenario. **/
	MOBSIM,

	/** Computes a score for each plans executed. **/
	SCORING,
	
	/** Rebuilds plans in order to improve computed score. **/
	REPLANNING
	
}
