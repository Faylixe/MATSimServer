package org.matsim.server.runtime;

import java.util.Optional;

/**
 * 
 * @author fv
 */
public final class MATSimRuntime {

	/** **/
	private static Optional<MATSimRuntime> RUNTIME = Optional.empty();

	/** **/
	private Optional<Simulation> simulation;

	/**
	 * Default constructor.
	 */
	private MATSimRuntime() {
		this.simulation = Optional.empty();
	}

	/**
	 * 
	 * @return
	 */
	public Optional<Simulation> getSimulation() {
		return simulation;
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
