package org.matsim.server;

import java.util.Optional;

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
		context.setContextPath("/");
		context.setClassLoader(ServletContainer.class.getClassLoader());
		final ServletHolder holder = context.addServlet(ServletContainer.class, "/*");
		holder.setInitOrder(0);
		holder.setInitParameter("jersey.config.server.provider.classnames", SimulationService.class.getCanonicalName());
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
		try {
			createServer();
		}
		catch (final Exception e) {
			// TODO : Log error.
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
				// TODO : Log error.
			}
		}
	}

}
