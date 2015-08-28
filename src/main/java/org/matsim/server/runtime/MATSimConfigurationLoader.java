package org.matsim.server.runtime;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.server.runtime.model.Simulation;

/**
 * A {@link MATSimConfigurationLoader} allows to load configuration file
 * into a valid MATSim {@link Config} instance. Where resources path
 * are normalizes to a given simulation container base path.
 * 
 * @author fv
 */
public final class MATSimConfigurationLoader implements Supplier<Config>{

	/** Path of configuration file to use. **/
	private static final String CONFIGURATION_PATH = "config.xml";

	/** Error message to throw when configuration file is not found. **/
	private static final String CONFIGURATION_NOT_FOUND = "No config.xml file found";

	/** Base path of the simulation container directory. **/
	private final Path base;

	/** Configuration that is built by this class. **/
	private final Config configuration;

	/**
	 * Default constructor.
	 * Creates an empty configuration from {@link ConfigUtils}.
	 * 
	 * @param base Base path of the simulation container directory.
	 */
	private MATSimConfigurationLoader(final Path base) {
		this.base = base;
		this.configuration = ConfigUtils.createConfig();
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.function.Supplier#get()
	 */
	@Override
	public Config get() {
		return configuration;
	}

	/**
	 * Loads the configuration file from base path.
	 * 
	 * @throws IllegalStateException If no configuration file has been found.
	 */
	private void load() {
		final Path path = base.resolve(CONFIGURATION_PATH);
		if (!Files.exists(path)) {
			throw new IllegalStateException(CONFIGURATION_NOT_FOUND);
		}
		ConfigUtils.loadConfig(configuration, path.toString());
	}

	/**
	 * Creates output directories and sets current configuration
	 * as outputing on it.
	 * 
	 * @throws IllegalStateException If any error occurs while creating output directories.
	 */
	private void configureOutput() {
		final Path output = base.resolve(Simulation.OUTPUT_PATH);
		if (!Files.exists(output)) {
			try {
				Files.createDirectories(output);
			}
			catch (final IOException e) {
				throw new IllegalStateException(e);
			}
		}
		configuration.controler().setOutputDirectory(output.toString());
	}

	/**
	 * Method that resolves a file provided by
	 * the given <tt>supplier</tt> and provides
	 * resolved path to the given <tt>consumer</tt>
	 * 
	 * @param supplier File name provider.
	 * @param consumer Resolved file path consumer.
	 */
	private void resolve(final Supplier<String> supplier, final Consumer<String> consumer) {
		final String origin = supplier.get();
		if (origin != null) {
			final Path resolved = base.resolve(Paths.get(origin));
			consumer.accept(resolved.toString());
		}
	}
	
	/**
	 * Resolves each potential external file
	 * regarding of the internal base path.
	 */
	private void resolve() {
		resolve(configuration.plans()::getInputFile, configuration.plans()::setInputFile);
		resolve(configuration.network()::getInputFile, configuration.network()::setInputFile);
		resolve(configuration.facilities()::getInputFile, configuration.facilities()::setInputFile);
	}

	/**
	 * Loads and returns a configuration file if
	 * any in the given <tt>base</tt> directory.
	 * 
	 * @return Loaded configuration.
	 * @throws IllegalStateException If configuration file cannot be found, or any error occurs during loading.
	 */
	public static Config load(final Path base) {
		final MATSimConfigurationLoader loader = new MATSimConfigurationLoader(base);
		loader.load();
		loader.configureOutput();
		loader.resolve();
		return loader.get();
	}

}
