package org.matsim.server.runtime;

import java.util.concurrent.atomic.AtomicInteger;

import org.matsim.core.controler.Controler;
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
 * 
 * @author fv
 */
public final class Simulation implements Runnable, StartupListener,
		ShutdownListener, IterationEndsListener, BeforeMobsimListener,
		ReplanningListener, ScoringListener {

	/** **/
	private static final AtomicInteger ID_FACTORY = new AtomicInteger();

	/** **/
	private final int id;

	/** **/
	private final SimulationState state;

	/** **/
	private final String path;

	/**
	 * 
	 */
	private Simulation(final int id, final String path) {
		this.id = id;
		this.path = path;
		this.state = new SimulationState();
	}

	/**
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		final Controler controler = new Controler(path);
		controler.run();
	}

	/**
	 * 
	 * @return
	 */
	public synchronized SimulationState getState() {
		return state;
	}

	/**
	 * 
	 * @param path
	 * @return
	 */
	public static Simulation createSimulation(final String path) {
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
		// Do nothing yet.
	}

	/*
	 * (non-Javadoc)
	 * @see org.matsim.core.controler.listener.StartupListener#notifyStartup(org.matsim.core.controler.events.StartupEvent)
	 */
	@Override
	public void notifyStartup(final StartupEvent event) {
		// Do nothing yet.
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
