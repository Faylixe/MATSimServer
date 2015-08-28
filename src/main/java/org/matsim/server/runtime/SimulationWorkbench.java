package org.matsim.server.runtime;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.matsim.server.common.ZipReader;
import org.matsim.server.exception.IllegalSimulationArchive;

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
	 * <b>Enhancement : Computing simulation input
	 * checksum in order to avoid duplication.</b>
	 * 
	 * @param simulation Simulation to register.
	 */
	public void registerSimulation(final Simulation simulation) {
		if (simulations.containsKey(simulation.getId())) {
			new IllegalStateException("Invalid simulation key provided");
		}
		simulations.put(simulation.getId(), simulation);
	}

	/**
	 * Creates and returns a list of all
	 * running simulation identifier.
	 * 
	 * @return Built identifier list.
	 */
	public List<Integer> getActiveSimulation() {
		return simulations
				.values()
				.stream()
				.filter(Simulation::isActive)
				.map(Simulation::getId)
				.collect(Collectors.toList());
	}

	/**
	 * Retrieves the simulation state for
	 * the given <tt>id</tt>.
	 * 
	 * @param id Index of the simulation state to retrieve.
	 * @return Empty optional if the given id is not valid, valid state otherwise.
	 */
	public Optional<SimulationState> getSimulationState(final int id) {
		if (!simulations.containsKey(id)) {
			return Optional.empty();
		}
		return Optional.of(simulations.get(id).getState());
	}
	
	/**
	 * 
	 * @return
	 * @throws FileNotFoundException 
	 */
	public Optional<FileInputStream> getOutput(final int id) throws FileNotFoundException {
		if (!simulations.containsKey(id)) {
			return Optional.empty();
		}
		final Simulation simulation = simulations.get(id);
		final File target = simulation.getOutputPath().toFile();
		final FileInputStream stream = new FileInputStream(target);
		return Optional.of(stream);
	}

	/**
	 * Factory method that creates a simulation instance from
	 * the given <tt>stream</tt>, which correspond to a user
	 * provided ZIP file which contains our simulation inputs.
	 * 
	 * @param stream Simulation input archive stream.
	 * @return Created simulation instance.
	 * @throws IllegalSimulationArchive If the given stream do not contains valid input file.
	 */
	public Simulation createSimulation(final InputStream stream) {
		try {
			final Path directory = FileContainer.createDirectory();
			final Path archive = directory.resolve(Paths.get(INPUT_NAME));
			Files.copy(stream, archive);
			ZipReader.extract(archive, directory);
			final Simulation simulation = Simulation.createSimulation(directory);
			registerSimulation(simulation);
			return simulation;
		}
		catch (final IOException e) {
			throw new IllegalSimulationArchive(e);
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
