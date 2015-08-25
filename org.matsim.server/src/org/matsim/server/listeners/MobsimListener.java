package org.matsim.server.listeners;

import org.matsim.core.controler.events.AfterMobsimEvent;
import org.matsim.core.controler.events.BeforeMobsimEvent;
import org.matsim.core.controler.listener.AfterMobsimListener;
import org.matsim.core.controler.listener.BeforeMobsimListener;

/**
 * 
 * @author fv
 */
public final class MobsimListener implements BeforeMobsimListener, AfterMobsimListener {

	/*
	 * (non-Javadoc)
	 * @see org.matsim.core.controler.listener.AfterMobsimListener#notifyAfterMobsim(org.matsim.core.controler.events.AfterMobsimEvent)
	 */
	@Override
	public void notifyAfterMobsim(final AfterMobsimEvent event) {
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.matsim.core.controler.listener.BeforeMobsimListener#notifyBeforeMobsim(org.matsim.core.controler.events.BeforeMobsimEvent)
	 */
	@Override
	public void notifyBeforeMobsim(final BeforeMobsimEvent event) {
		
	}

}
