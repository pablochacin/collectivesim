package edu.upc.cnds.collectivesim.models.imp;

import java.util.logging.Logger;

import uchicago.src.sim.engine.BasicAction;
import uchicago.src.sim.engine.Schedule;

public abstract class ScheduledAction extends BasicAction {

	
	protected static Logger log = Logger.getLogger("collectivesim.models");
	
	protected Schedule schedule;
		
	public ScheduledAction(Schedule schedule) {
		this.schedule = schedule;
	}
	
	@Override
	public abstract void execute();
	
	
    public void cancel() {
   	 schedule.removeAction(this);
    }
    

}
