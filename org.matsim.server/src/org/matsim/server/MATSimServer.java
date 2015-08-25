package org.matsim.server;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 * 
 * @author fv
 */
public final class MATSimServer implements IApplication {

	/** Default server port to use. **/
	private static final int DEFAULT_PORT = 8080;

	/** **/
	private static final String RESOURCE_PROPERTY = "om.sun.jersey.config.property.resourceConfigClass";

	/** **/
	private static final String PACKAGES_PROPERTY = "com.sun.jersey.config.property.packages";

	/** **/
	private static final String MAPPING_PROPERTY = "com.sun.jersey.api.json.POJOMappingFeature";

	/** **/
	private static final String RESOURCE_CLASS = "com.sun.jersey.api.core.PackagesResourceConfig";
	
	/** **/
	private static final String PACKAGE = "org.matsim.server.services";

	/**
	 * 
	 * @return
	 */
	private ServletHolder createServletHolder() {
		final ServletHolder holder = new ServletHolder(ServletContainer.class);    
		holder.setInitParameter(RESOURCE_PROPERTY, RESOURCE_CLASS);
		holder.setInitParameter(PACKAGES_PROPERTY, PACKAGE);
		holder.setInitParameter(MAPPING_PROPERTY, Boolean.TRUE.toString());
		return holder;
	}

	/**
	 * 
	 * @throws Exception
	 */
	private void createServer() throws Exception {
		final Server server = new Server(DEFAULT_PORT);
		final ServletContextHandler context = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);
		context.addServlet(createServletHolder(), "/*");
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
