package org.matsim.server.common;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;

/**
 * Small utility class that allows to read
 * and extracts files from a ZIP archive.
 * 
 * @author fv
 */
public final class ZipReader {

	/** ZIP input stream to read. **/
	private final ZipArchiveInputStream stream;

	/**
	 * Default constructor. Builds a
	 * {@link ZipArchiveInputStream} from the given
	 * stream.
	 * 
	 * @param stream Target input stream to read and extract. 
	 */
	public ZipReader(final InputStream stream) {
		this.stream = new ZipArchiveInputStream(stream);
	}

	/**
	 * Extracts all files from the internal stream
	 * into the given <tt>target</tt> directory.
	 * 
	 * @param target Target directory to extract file into.
	 * @throws IOException If any error occurs while reading the archive.
	 */
	public void extract(final Path target) throws IOException {
		Optional<ZipArchiveEntry> current = Optional.ofNullable(stream.getNextZipEntry());
		while (current.isPresent()) {
			final ZipArchiveEntry entry = current.get();
			final Path file = Paths.get(entry.getName());
			final Path path = target.resolve(file);
			Files.copy(stream, path);
			current = Optional.ofNullable(stream.getNextZipEntry());
		}
	}
	
}
