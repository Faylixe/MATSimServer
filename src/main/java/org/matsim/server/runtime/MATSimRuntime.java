package org.matsim.server.runtime;

import java.util.Optional;

import org.matsim.core.controler.Controler;
import org.matsim.server.runtime.model.Simulation;

/**
 * {@link MATSimRuntime} is a simulation execution container.
 * It allows to run a given {@link Simulation} in a dedicated
 * thread.
 * 
 * @author fv
 */
public final class MATSimRuntime {

	/** Unique runtime instance defined as empty optional until created. **/
	private static Optional<MATSimRuntime> RUNTIME = Optional.empty();

	/**
	 * Default constructor.
	 */
	private MATSimRuntime() {
		
	}

	/**
	 * TODO :
	 * @param simulation
	 */
	private void run(final Simulation simulation) {
		final Controler controler = new Controler("");
		controler.addControlerListener(simulation);
		controler.run();		
	}

	/**
	 * Static method that commit the given <tt>simulation</tt>
	 * to be ran by the unique runtime instance.
	 * 
	 * @param simulation Simulation to commit.
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
