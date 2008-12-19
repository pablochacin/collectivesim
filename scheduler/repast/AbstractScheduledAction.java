package edu.upc.cnds.collectivesim.scheduler.repast;

import java.util.logging.Logger;

import edu.upc.cnds.collectivesim.scheduler.ScheduledAction;

import uchicago.src.sim.engine.BasicAction;
import uchicago.src.sim.engine.Schedule;

public abstract class AbstractScheduledAction extends BasicAction implements ScheduledAction {

	
	protected static Logger log = Logger.getLogger("collectivesim.models");
	
	protected Schedule schedule;
		
	public AbstractScheduledAction(Schedule schedule) {
		this.schedule = schedule;
	}
	
	@Override
	public abstract void execute();
	
	
    /* (non-Javadoc)
	 * @see edu.upc.cnds.collectivesim.models.imp.ScheduledAction#cancel()
	 */
    public void cancel() {
   	 schedule.removeAction(this);
    }
    

}
