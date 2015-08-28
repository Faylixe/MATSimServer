package org.matsim.server.common;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.log4j.Logger;

/**
 * Small utility class that allows to read
 * and extracts files from a ZIP archive.
 * 
 * @author fv
 */
public final class ZipReader {
	
	/** Class logger. **/
	private static final Logger LOG = Logger.getLogger(ZipReader.class);

	/** ZIP input stream to read. **/
	private final ZipArchiveInputStream stream;

	/**
	 * Default constructor. Builds a
	 * {@link ZipArchiveInputStream} from the given
	 * stream.
	 * 
	 * @param stream Target input stream to read and extract. 
	 */
	private ZipReader(final InputStream stream) {
		this.stream = new ZipArchiveInputStream(stream);
	}

	/**
	 * Extracts all files from the internal stream
	 * into the given <tt>target</tt> directory.
	 * 
	 * @param destination Target directory to extract file into.
	 * @throws IOException If any error occurs while reading the archive.
	 */
	private void extract(final Path destination) throws IOException {
		Optional<ZipArchiveEntry> current = Optional.ofNullable(stream.getNextZipEntry());
		while (current.isPresent()) {
			final ZipArchiveEntry entry = current.get();
			final Path file = Paths.get(entry.getName());
			final Path path = destination.resolve(file);
			if (entry.isDirectory()) {
				Files.createDirectories(path);
				LOG.info("Creating directory" + path.toString());
			}
			else {
				Files.copy(stream, path);
				LOG.info("Writing file " + path.toString());
			}
			current = Optional.ofNullable(stream.getNextZipEntry());
		}
	}

	/**
	 * Static shortcut method for extracting a given ZIP archive
	 * to the given <tt>destination</tt>.
	 * 
	 * @param source Source archive to extract.
	 * @param destination Destination path.
	 * @throws IOException if any error occurs while extracting files.
	 */
	public static void extract(final Path source, final Path destination) throws IOException {
		final InputStream stream = Files.newInputStream(source);
		final ZipReader reader = new ZipReader(stream);
		reader.extract(destination);
	}

}
