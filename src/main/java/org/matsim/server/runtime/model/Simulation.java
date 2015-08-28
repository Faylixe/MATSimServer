package org.matsim.server.runtime.model;

import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;

import org.matsim.core.controler.events.BeforeMobsimEvent;
import org.matsim.core.controler.events.IterationEndsEvent;
import org.matsim.core.controler.events.ReplanningEvent;
import org.matsim.core.controler.events.ScoringEvent;
import org.matsim.core.controler.events.ShutdownEvent;
import org.matsim.core.controler.events.StartupEvent;
import org.matsim.core.controler.listener.BeforeMobsimListener;
import org.matsim.core.controler.listener.IterationEndsListener;
import org.matsim.core.controler.listener.ReplanningListener;
import org.matsim.core.controler.listener.ScoringListener;
import org.matsim.core.controler.listener.ShutdownListener;
import org.matsim.core.controler.listener.StartupListener;

/**
 * Model representation for a simulation instance available
 * on this server. Such instance are defined by a unique identifier
 * and a working directory.
 * 
 * @author fv
 */
public final class Simulation implements StartupListener, ShutdownListener,
		IterationEndsListener, BeforeMobsimListener, ReplanningListener,
		ScoringListener {

	/** Counter for creating Simulation unique index. **/
	private static final AtomicInteger ID_FACTORY = new AtomicInteger();

	/** Simulation identifier. **/
	private final int id;

	/** State of this simulation. **/
	private final SimulationState state;

	/** Target directory this simulation is registered into. **/
	private final Path path;

	/** Boolean flag that indicates if this simulation is active (namely running) or not. **/
	private boolean active;

	/**
	 * Default constructor.
	 * 
	 * @param id Simulation identifier.
	 * @param path Target directory this simulation is registered into.
	 */
	private Simulation(final int id, final Path path) {
		this.id = id;
		this.path = path;
		this.active = false;
		this.state = new SimulationState();
	}

	/**
	 * Identifier getter.
	 * 
	 * @return Simulation identifier.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Working directory getter.
	 * 
	 * @return Path of the working directory.
	 */
	public Path getPath() {
		return path;
	}
	/**
	 * Indicates if this simulation is active or not.
	 * 
	 * @return <tt>true</tt> if this simulation is running, <tt>false</tt> otherwise.
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Synchronized access to the simulation state. 
	 * 
	 * @return State of this simulation.
	 */
	public synchronized SimulationState getState() {
		return state;
	}

	/**
	 * Static factory method for creating unique
	 * identified simulation instance.
	 * 
	 * @param path Target directory this simulation is registered into.
	 * @return Created simulation instance.
	 */
	public static Simulation createSimulation(final Path path) {
		final int id = ID_FACTORY.getAndIncrement();
		return new Simulation(id, path);
	}

	/*
	 * (non-Javadoc)
	 * @see org.matsim.core.controler.listener.IterationEndsListener#notifyIterationEnds(org.matsim.core.controler.events.IterationEndsEvent)
	 */
	@Override
	public void notifyIterationEnds(final IterationEndsEvent event) {
		getState().incrementIteration();
	}

	/*
	 * (non-Javadoc)
	 * @see org.matsim.core.controler.listener.ShutdownListener#notifyShutdown(org.matsim.core.controler.events.ShutdownEvent)
	 */
	@Override
	public void notifyShutdown(final ShutdownEvent event) {
		getState().deleteIteration();
	}

	/*
	 * (non-Javadoc)
	 * @see org.matsim.core.controler.listener.StartupListener#notifyStartup(org.matsim.core.controler.events.StartupEvent)
	 */
	@Override
	public void notifyStartup(final StartupEvent event) {
		getState().createIteration();
	}

	/*
	 * (non-Javadoc)
	 * @see org.matsim.core.controler.listener.ScoringListener#notifyScoring(org.matsim.core.controler.events.ScoringEvent)
	 */
	@Override
	public void notifyScoring(final ScoringEvent event) {
		getState().setPhase(SimulationPhase.SCORING);
	}

	/*
	 * (non-Javadoc)
	 * @see org.matsim.core.controler.listener.BeforeMobsimListener#notifyBeforeMobsim(org.matsim.core.controler.events.BeforeMobsimEvent)
	 */
	@Override
	public void notifyBeforeMobsim(final BeforeMobsimEvent event) {
		getState().setPhase(SimulationPhase.MOBSIM);
	}

	/*
	 * (non-Javadoc)
	 * @see org.matsim.core.controler.listener.ReplanningListener#notifyReplanning(org.matsim.core.controler.events.ReplanningEvent)
	 */
	@Override
	public void notifyReplanning(final ReplanningEvent event) {
		getState().setPhase(SimulationPhase.REPLANNING);
	}

}
