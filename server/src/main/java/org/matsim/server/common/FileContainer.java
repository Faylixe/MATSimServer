package org.matsim.server.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

/**
 * 
 * @author fv
 */
public final class FileContainer {

	/** **/
	private static Optional<FileContainer> INSTANCE = Optional.empty();

	/** **/
	private final Path root;

	/**
	 * 
	 */
	private FileContainer() {
		// TODO : Check for folder parameters.
		this.root = null;
	}

	/**
	 * 
	 * @return
	 * @throws IOException 
	 */
	public Path create() throws IOException {
		final String uuid = UUID.randomUUID().toString();
		final Path path = root.resolve(Paths.get(uuid));
		Files.createDirectory(path);
		return path;
	}

	/**
	 * 
	 * @return
	 * @throws IOException 
	 */
	public static Path createDirectory() throws IOException {
		synchronized (FileContainer.class) {
			if (!INSTANCE.isPresent()) {
				INSTANCE = Optional.of(new FileContainer());
			}
		}
		return INSTANCE.get().create();
	}

}
