package org.matsim.server;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.apache.log4j.Logger;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.matsim.server.service.SimulationService;

/**
 * {@link MATSimServer} application is the entry point
 * of a simulation runtime server. It starts a jetty server
 * using servlet instance as RESTFull service.
 * 
 * @author fv
 */
public final class MATSimServer extends ResourceConfig  {

	/** Class logger. **/
	private static final Logger LOG = Logger.getLogger(MATSimServer.class.getName());

	/**
	 * Default constructor.
	 * Initializes web service instance as empty optional.
	 */
	public MATSimServer() {
		register(MultiPartFeature.class);
		register(SimulationService.class);
	}

	/**
	 * TODO : Javadoc.
	 * 
	 * @param args
	 */
	public static void main(final String [] args) {
		// Build URI.
		final UriBuilder builder = UriBuilder.fromUri(MATSimServerConstants.DEFAULT_HOSTNAME);
		builder.port(MATSimServerConstants.DEFAULT_PORT);
		final URI uri = builder.build();
		// Build server context and runs it.
		final MATSimServer server = new MATSimServer();
		try {
			JettyHttpContainerFactory.createServer(uri, server);
		}
		catch (final Exception e) {
			// TODO : Improves error logging here.
			LOG.error("An error occurs : " + e.getMessage());
		}
	}

}
