package org.matsim.server.runtime;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.matsim.core.config.Config;
import org.matsim.core.controler.Controler;
import org.matsim.server.runtime.model.Simulation;

/**
 * {@link MATSimRuntime} is a simulation execution container.
 * It allows to run a given {@link Simulation} in a dedicated
 * thread.
 * 
 * @author fv
 */
public final class MATSimRuntime implements ThreadFactory {

	/** Class logger. **/
	private static final Logger LOG = Logger.getLogger(MATSimRuntime.class);

	/** Prefix used for simulation thread. **/
	private static final String THREAD_PREFIX = "simulation-";

	/** Unique runtime instance defined as empty optional until created. **/
	private static Optional<MATSimRuntime> RUNTIME = Optional.empty();

	/** Thread executors used as simulation sandbox. **/
	private final ExecutorService executor;

	/** Atomic integer that contains current thread index. **/
	private final AtomicInteger counter;

	/**
	 * Default constructor.
	 * Initializes internal thread pool.
	 */
	private MATSimRuntime() {
		this.counter = new AtomicInteger();
		this.executor = Executors.newCachedThreadPool(this);
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.concurrent.ThreadFactory#newThread(java.lang.Runnable)
	 */
	@Override
	public Thread newThread(final Runnable runnable) {
		final StringBuilder builder = new StringBuilder();
		builder.append(THREAD_PREFIX);
		builder.append(counter.getAndIncrement());
		return new Thread(runnable, builder.toString());
	}

	/**
	 * Loads and runs the given <tt>simulation</tt> into
	 * a separated thread which acts as simulation sandbox.
	 * 
	 * @param simulation Simulation to run.
	 */
	private void run(final Simulation simulation) {
		final Config configuration = MATSimConfigurationLoader.load(simulation.getPath());
		final Controler controler = new Controler(configuration);
		controler.addControlerListener(simulation);
		LOG.info("Start simulation #" + simulation.getId());
		executor.execute(() -> {
			controler.run();
		});
	}

	/**
	 * Static method that commit the given <tt>simulation</tt>
	 * to be ran by the unique runtime instance.
	 * 
	 * @param simulation Simulation to commit.
	 */
	public static void commit(final Simulation simulation) {
		synchronized (MATSimRuntime.class) {
			if (!RUNTIME.isPresent()) {
				RUNTIME = Optional.of(new MATSimRuntime());
			}
		}
		RUNTIME.get().run(simulation);
	}

}
