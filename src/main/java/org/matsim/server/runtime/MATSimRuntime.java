package org.matsim.server.runtime;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.apache.log4j.Logger;
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

	/** Class logger. **/
	private static final Logger LOG = Logger.getLogger(MATSimRuntime.class);

	/** Path of configuration file to use. **/
	private static final String CONFIGURATION_PATH = "config.xml";

	/** Error message to throw when configuration file is not found. **/
	private static final String CONFIGURATION_NOT_FOUND = "No config.xml file found";

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
		final Path configuration = simulation.getPath().resolve(CONFIGURATION_PATH);
		if (!Files.exists(configuration)) {
			throw new IllegalStateException(CONFIGURATION_NOT_FOUND);
		}
		LOG.info("Set working directory to : " + simulation.getPath().toAbsolutePath().toString());
		System.setProperty("user.dir", simulation.getPath().toAbsolutePath().toString());
		final Controler controler = new Controler(configuration.toString());
		controler.addControlerListener(simulation);
		LOG.info("Start simulation #" + simulation.getId());
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
		RUNTIME.get().run(simulation);
	}

}
