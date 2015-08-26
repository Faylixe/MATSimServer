package org.matsim.server;

import java.util.Optional;
import java.util.logging.Logger;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import org.matsim.server.service.SimulationService;

/**
 * {@link MATSimServer} application is the entry point
 * of a simulation runtime server. It starts a jetty server
 * using servlet instance as RESTFull service.
 * 
 * @author fv
 */
public final class MATSimServer implements IApplication {

	/** Default server port to use. **/
	private static final int DEFAULT_PORT = 8080;

	/** Parameters key for the {@link ServletHolder} instance. **/
	private static final String INIT_PARAMETER = "jersey.config.server.provider.classnames";

	/** {@link ServletHolder} supported path. **/
	private static final String PATH_SPEC = "/*";

	/** Context path for our servlet context. **/
	private static final String CONTEXT_PATH = "/";

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
	}

	/**
	 * Creates our server handler that will
	 * contains our RESTFull servlet.
	 * 
	 * @return Created {@link ServletContextHandler} instance.
	 */
	private ServletContextHandler createHandler() {
		final ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath(CONTEXT_PATH);
		context.setClassLoader(ServletContainer.class.getClassLoader());
		final ServletHolder holder = context.addServlet(ServletContainer.class, PATH_SPEC);
		holder.setInitOrder(0);
		holder.setInitParameter(INIT_PARAMETER, SimulationService.class.getCanonicalName());
		return context;
	}

	/**
	 * Creates server instance and configures
	 * it content.
	 * 
	 * @throws Exception If any error occurs while starting server.
	 */
	private void createServer() throws Exception {
		webservice = Optional.of(new Server(DEFAULT_PORT));
		if (webservice.isPresent()) {
			final Server server = webservice.get();
			server.setHandler(createHandler());
			server.start();
			server.join();
		}
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
