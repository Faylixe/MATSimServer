package org.matsim.server.runtime;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.matsim.server.common.FileContainer;
import org.matsim.server.common.ZipReader;
import org.matsim.server.runtime.model.Simulation;

/**
 * {@link SimulationWorkbench} contains all simulations
 * this server contains, which could be running or not.
 * 
 * @author fv
 */
public final class SimulationWorkbench {

	/** Default name for submitted input ZIP archive. **/
	private static final String INPUT_NAME = "input.zip";

	/** Unique workbench instance defined as empty optional until created. **/
	private static Optional<SimulationWorkbench> WORKBENCH = Optional.empty();

	/** Index of simulation instance available in this web service. **/
	private final Map<Integer, Simulation> simulations;

	/**
	 * Default constructor.
	 */
	private SimulationWorkbench() {
		this.simulations = new HashMap<Integer, Simulation>();
	}
	
	/**
	 * Register the given simulation into this workbench.
	 * Ensuring it is not existing yet.
	 * 
	 * <note>Enhancement : Computing simulation input
	 * checksum in order to avoid duplication.</note>
	 * 
	 * @param simulation Simulation to register.
	 */
	public void registerSimulation(final Simulation simulation) {
		if (simulations.containsKey(simulation.getId())) {
			// TODO : Throw exception ?
		}
		simulations.put(simulation.getId(), simulation);
	}

	/**
	 * Creates and returns a {@link Stream} of all available
	 * simulation instance.
	 * 
	 * @return Built {@link Stream}.
	 */
	public Stream<Simulation> getSimulations() {
		return simulations.values().stream();
	}

	/**
	 * Retrieves the simulation for the given <tt>id</tt>.
	 * 
	 * @param id Index of the simulation to retrieve.
	 * @return Empty optional if the given id is not valid, valid Simulation otherwise.
	 */
	public Optional<Simulation> getSimulation(final int id) {
		return Optional.ofNullable(simulations.get(id));
	}

	/**
	 * Factory method that creates a simulation instance from
	 * the given <tt>stream</tt>, which correspond to a user
	 * provided ZIP file which contains our simulation inputs.
	 * 
	 * @return Created simulation instance.
	 * @throws IllegalSimulationArchive If the given stream do not contains valid input file.
	 */
	public Simulation createSimulation(final InputStream stream) {
		try {
			final Path directory = FileContainer.createDirectory();
			Files.copy(stream, directory.resolve(Paths.get(INPUT_NAME)));
			final ZipReader reader = new ZipReader(stream);
			reader.extract(directory);
			return Simulation.createSimulation(directory);
		}
		catch (final IOException e) {
			// TODO : Custom error.
			throw new IllegalSimulationArchive();
		}
	}

	/**
	 * Static method that retrieves the unique workbench instance.
	 * If not existing, the instance will be created (thread-safe).
	 * 
	 * @return Unique workbench instance.
	 */
	public static SimulationWorkbench getInstance() {
		synchronized (SimulationWorkbench.class) {
			if (!WORKBENCH.isPresent()) {
				WORKBENCH = Optional.of(new SimulationWorkbench());
			}
		}
		return WORKBENCH.get();
	}
	
}
