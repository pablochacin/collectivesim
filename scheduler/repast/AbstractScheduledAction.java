package edu.upc.cnds.collectivesim.scheduler.repast;

import java.util.logging.Logger;

import edu.upc.cnds.collectivesim.scheduler.ScheduledAction;

import uchicago.src.sim.engine.BasicAction;
import uchicago.src.sim.engine.Schedule;

public abstract class AbstractScheduledAction extends BasicAction implements ScheduledAction {

	
	protected static Logger log = Logger.getLogger("collectivesim.model");
	
	protected RepastScheduler scheduler;
	
	/**
	 * object on which the action will be executed
	 */
	protected Runnable target;
	

	
	public AbstractScheduledAction(RepastScheduler scheduler,Runnable target) {
		this.scheduler = scheduler;
		this.target = target;
	}
	
	@Override
	public void execute(){
   	 	log.finest("[" + scheduler.getTime() + "]" + target.getClass().getName());
		doExecute();
	}
	
	
	public abstract void doExecute();
	
    /* (non-Javadoc)
	 * @see edu.upc.cnds.collectivesim.models.imp.ScheduledAction#cancel()
	 */
    public void cancel() {
    	//notify the repastScheduler that this action must be cancelled
    	scheduler.cancelAction(this);
    }
    

	/**
	 * Return target object.
	 * 
	 */
     public Object getTarget(){
    	 return this.target;
     }
}
