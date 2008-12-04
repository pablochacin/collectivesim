package edu.upc.cnds.collectivesim.models.imp;

import java.util.logging.Logger;

import edu.upc.cnds.collectivesim.models.ScheduledAction;

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
