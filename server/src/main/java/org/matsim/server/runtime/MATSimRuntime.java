package org.matsim.server.runtime;

import java.util.Optional;

import org.matsim.core.controler.Controler;
import org.matsim.server.runtime.model.Simulation;

/**
 * 
 * @author fv
 */
public final class MATSimRuntime {

	/** **/
	private static Optional<MATSimRuntime> RUNTIME = Optional.empty();

	/**
	 * 
	 */
	private MATSimRuntime() {
		
	}

	/**
	 * 
	 * @param simulation
	 */
	private void run(final Simulation simulation) {
		final Controler controler = new Controler("");
		controler.addControlerListener(simulation);
		controler.run();		
	}

	/**
	 * 
	 * @param simulation
	 */
	public static void commit(final Simulation simulation) {
		synchronized (MATSimRuntime.class) {
			if (!RUNTIME.isPresent()) {
				RUNTIME = Optional.of(new MATSimRuntime());
			}
		}
		RUNTIME.get();
	}

}
