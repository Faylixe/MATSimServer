package org.matsim.server.runtime;

/**
 * Enumeration of simulation life phase.
 * 
 * @author fv
 */
public enum SimulationPhase {

	/** The simulation phase that runs all agents's scenario. **/
	MOBSIM,

	/** Computes a score for each plans executed. **/
	SCORING,
	
	/** Rebuilds plans in order to improve computed score. **/
	REPLANNING
	
}
