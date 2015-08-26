package org.matsim.server;

/**
 * This class contains all static
 * constants used by our server.
 * 
 * @author fv
 */
public final class MATSimServerConstants {

	/**
	 * Private coonstructor for
	 * avoiding instantiation.
	 */
	private MATSimServerConstants() {
	}

	/** Default server port to use. **/
	public static final int DEFAULT_PORT = 8080;

	/** Default server host name to use. **/
	public static final String DEFAULT_HOSTNAME = "http://localhost";

	/** Default container directory to use. **/
	public static final String DEFAULT_CONTAINER = ".container";

	/** System property key,  **/
	public static final String CONTAINER_PROPERTY = "org.matsim.server.container";

	/** Application type used for error message. **/
	public static final String ERROR_RESPONSE_TYPE = "application/xml";

}
