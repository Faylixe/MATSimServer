package org.matsim.server.runtime;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 
 * @author fv
 */
public final class MATSimRuntime {

	/** **/
	private static Optional<MATSimRuntime> RUNTIME = Optional.empty();

	/** **/
	private final Map<Integer, Simulation> simulations;

	/**
	 * Default constructor.
	 */
	private MATSimRuntime() {
		this.simulations = new HashMap<Integer, Simulation>();
	}
	
	/**
	 * 
	 * @param simulation
	 */
	public void registerSimulation(final Simulation simulation) {
		if (simulations.containsKey(simulation.getId())) {
			// TODO : Throw exception ?
		}
		simulations.put(simulation.getId(), simulation);
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public Optional<Simulation> getSimulation(final int id) {
		return Optional.ofNullable(simulations.get(id));
	}

	/**
	 * 
	 * @return
	 */
	public static MATSimRuntime getInstance() {
		synchronized (MATSimRuntime.class) {
			if (!RUNTIME.isPresent()) {
				RUNTIME = Optional.of(new MATSimRuntime());
			}
		}
		return RUNTIME.get();
	}
	
}
