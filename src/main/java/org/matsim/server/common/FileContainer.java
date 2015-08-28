package org.matsim.server.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import org.matsim.server.MATSimServerConstants;

/**
 * {@link FileContainer} represents our server
 * working directory, on which file are uploaded
 * and created. It uses the system property defined by
 * {@link MATSimServerConstants#CONTAINER_PROPERTY} in order
 * to locate the target directory to use.
 * 
 * @author fv
 */
public final class FileContainer {

	/** Unique instance of our {@link FileContainer}. **/
	private static Optional<FileContainer> INSTANCE = Optional.empty();

	/** Root path of our wokring directory. **/
	private final Path root;

	/**
	 * Default constructor.
	 * Initializes our root path by parsing the system properties.
	 */
	private FileContainer() {
		final String target = System.getProperty(MATSimServerConstants.CONTAINER_PROPERTY);
		this.root = Paths.get(target);
	}

	/**
	 * Ensures that the target root directory exist
	 * and creates it otherwise.
	 * 
	 * @throws IOException If any error occurs while creating root directories.
	 */
	private void initialize() throws IOException {
		if (!Files.exists(root)) {
			Files.createDirectories(root);
		}
	}

	/**
	 * Creates a new working directory child
	 * using a {@link UUID} as directory name.
	 * 
	 * @return Path of the created directory.
	 * @throws IOException If any error occurs while creating directory.
	 */
	public Path create() throws IOException {
		final String uuid = UUID.randomUUID().toString();
		final Path path = root.resolve(Paths.get(uuid));
		Files.createDirectory(path);
		return path;
	}

	/**
	 * Static factory method that allows to create a new directory
	 * into the server working directory. If {@link FileContainer}
	 * instance does not exist it will be created (thread-safe).
	 * 
	 * @return Path of the created directory.
	 * @throws IOException If any error occurs while creating directory.
	 */
	public static Path createDirectory() throws IOException {
		synchronized (FileContainer.class) {
			if (!INSTANCE.isPresent()) {
				INSTANCE = Optional.of(new FileContainer());
				INSTANCE.get().initialize();
			}
		}
		return INSTANCE.get().create();
	}

}
