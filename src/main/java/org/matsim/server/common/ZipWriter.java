package org.matsim.server.common;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.log4j.Logger;

/**
 * Small utility class that allows to write
 * a given directory content to a ZIP archive.
 * 
 * @author fv
 */
public final class ZipWriter {

	/** Class logger. **/
	private static final Logger LOG = Logger.getLogger(ZipWriter.class);

	/** ZIP output stream to write into. **/
	private final ZipArchiveOutputStream stream;

	/**
	 * Default constructor. Builds a
	 * {@link ZipArchiveOutputStream} from
	 * the given stream.
	 * 
	 * @param stream Target output stream to write into.
	 */
	private ZipWriter(final OutputStream stream) {
		this.stream = new ZipArchiveOutputStream(stream);
	}

	/**
	 * 
	 * @param directory
	 */
	private void compressDirectory(final Path directory) {
		try {
			Files.newDirectoryStream(directory).forEach(child -> {
				compress(child);
			});
		}
		catch (final IOException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Compresses the given <tt>source</tt> file by
	 * creating the associated archive entry and writes
	 * it content into the internal stream.
	 * 
	 * @param source Source file to compress.
	 * @throws IOException If any error occurs while writing file content.
	 */
	private void compress(final Path source) {
		if (!Files.isDirectory(source)) {
			LOG.info("Writing file entry " + source);
			final File file = source.toFile();
			final ZipArchiveEntry entry = new ZipArchiveEntry(file, file.getName());
			try {
				stream.putArchiveEntry(entry);
				Files.copy(source, stream);
				stream.closeArchiveEntry();
			}
			catch (final IOException e) {
				throw new IllegalStateException(e);
			}
		}
		else {
			compressDirectory(source);
		}
	}

	/**
	 * Finalizes archive writing and closes
	 * the internal stream.
	 * 
	 * @throws IOException If any error occurs while saving the file.
	 */
	private void save() throws IOException {
		stream.flush();
		stream.finish();
		stream.close();
	}

	/**
	 * 
	 * Static shortcut method for creating a ZIP
	 * archive from the given <tt>source</tt>
	 * directory content.
	 * 
	 * @param source Source directory to write.
	 * @param destination Destination archive.
	 * @throws IOException if any error occurs while writing files.
	 */
	public static void compress(final Path source, final Path destination) throws IOException {
		final OutputStream stream = Files.newOutputStream(destination);
		final ZipWriter writer = new ZipWriter(stream);
		try {
			Files.newDirectoryStream(source).forEach(writer::compress);
		}
		catch (final IllegalStateException e) {
			final Throwable cause = e.getCause();
			if (cause instanceof IOException) {
				throw (IOException) cause;				
			}
		}
		writer.save();
	}

}
