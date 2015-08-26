package org.matsim.server;

import java.net.URI;
import java.util.Optional;
import java.util.logging.Logger;

import javax.ws.rs.core.UriBuilder;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jetty.server.Server;
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
public final class MATSimServer extends ResourceConfig implements IApplication {

	/** Default server port to use. **/
	private static final int DEFAULT_PORT = 8080;

	/** **/
	private static final String DEFAULT_HOSTNAME = "http://localhost";

	/** Java property key for the logger format. **/
	private static final String LOGGER_FORMAT_PROPERTY = "java.util.logging.SimpleFormatter.format";

	/** Logger formatting pattern used. **/
	private static final String LOGGER_FORMAT_PATTERN = "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %4$-6s %2$s %5$s%6$s%n";

	/** Class logger. **/
	private static final Logger LOG = Logger.getLogger(MATSimServer.class.getName());

	/** Internal web service instance. **/
	private Optional<Server> webservice;

	/**
	 * Default constructor.
	 * Initializes web service instance as empty optional.
	 */
	public MATSimServer() {
		this.webservice = Optional.empty();
		register(MultiPartFeature.class);
	}

	/**
	 * Creates server instance and configures
	 * it content.
	 * 
	 * @throws Exception If any error occurs while starting server.
	 */
	private void createServer() throws Exception {
		final URI uri = UriBuilder.fromUri(DEFAULT_HOSTNAME).port(DEFAULT_PORT).build();
		final Server server = JettyHttpContainerFactory.createServer(uri, this);
		server.start();
		this.webservice = Optional.of(server);
		server.join();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext)
	 */
	@Override
	public Object start(final IApplicationContext context) throws Exception {
		System.setProperty(LOGGER_FORMAT_PROPERTY, LOGGER_FORMAT_PATTERN);
		try {
			createServer();
		}
		catch (final Exception e) {
			LOG.severe("An unexpected error occurs while creating server instance : " + e.getMessage());
		}
		return IApplication.EXIT_OK;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	@Override
	public void stop() {
		if (webservice.isPresent()) {
			try {
				webservice.get().stop();
			}
			catch (final Exception e) {
				LOG.severe("An unexpected error occurs while stoping server instance : " + e.getMessage());
			}
		}
	}

}
