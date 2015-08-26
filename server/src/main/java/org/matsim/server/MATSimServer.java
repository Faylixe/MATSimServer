package org.matsim.server;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;

/**
 * {@link MATSimServer} application is the entry point
 * of a simulation runtime server. It starts a jetty server
 * using servlet instance as RESTFull service.
 * 
 * @author fv
 */
public final class MATSimServer extends ResourceConfig  {

	/** Default server port to use. **/
	private static final int DEFAULT_PORT = 8080;

	/** **/
	private static final String DEFAULT_HOSTNAME = "http://localhost";

	/** Java property key for the logger format. **/
	private static final String LOGGER_FORMAT_PROPERTY = "java.util.logging.SimpleFormatter.format";

	/** Logger formatting pattern used. **/
	private static final String LOGGER_FORMAT_PATTERN = "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %4$-6s %2$s %5$s%6$s%n";

	/** Class logger. **/
	//private static final Logger LOG = Logger.getLogger(MATSimServer.class.getName());

	/**
	 * Default constructor.
	 * Initializes web service instance as empty optional.
	 */
	public MATSimServer() {
		register(MultiPartFeature.class);
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(final String [] args) {
		System.setProperty(LOGGER_FORMAT_PROPERTY, LOGGER_FORMAT_PATTERN);
		final URI uri = UriBuilder.fromUri(DEFAULT_HOSTNAME).port(DEFAULT_PORT).build();
		final MATSimServer server = new MATSimServer();
		JettyHttpContainerFactory.createServer(uri, server);
	}

}
