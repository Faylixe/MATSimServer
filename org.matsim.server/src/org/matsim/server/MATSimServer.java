package org.matsim.server;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import org.matsim.server.services.SimulationService;
import org.osgi.framework.wiring.BundleWiring;

/**
 * 
 * @author fv
 */
public final class MATSimServer implements IApplication {

	/** Default server port to use. **/
	private static final int DEFAULT_PORT = 8080;

	/**
	 * 
	 * @throws Exception
	 */
	private void createServer() throws Exception {
		final Server server = new Server(DEFAULT_PORT);
		final ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		context.setClassLoader(ServletContainer.class.getClassLoader());
		server.setHandler(context);
		final ServletHolder holder = context.addServlet(ServletContainer.class, "/*");
		holder.setInitOrder(0);
		holder.setInitParameter("jersey.config.server.provider.classnames", SimulationService.class.getCanonicalName());
		server.start();
		server.join();
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
		// Finalizes pending simulation.
	}

}
