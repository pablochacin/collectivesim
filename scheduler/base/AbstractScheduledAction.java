package edu.upc.cnds.collectivesim.scheduler.base;

import java.util.logging.Logger;

import edu.upc.cnds.collectivesim.scheduler.ScheduledAction;



public abstract class AbstractScheduledAction implements ScheduledAction, Runnable, Comparable<AbstractScheduledAction> {

	
	protected static Logger log = Logger.getLogger("collectivesim.scheduler.base");
	
	
	protected AbstractScheduler scheduler;
	
	/**
	 * object on which the action will be executed
	 */
	protected Runnable target;
	
	
	public AbstractScheduledAction(AbstractScheduler scheduler,Runnable target) {
		this.scheduler = scheduler;
		this.target = target;
	}
	
	public void run(){
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
     
     
     /**
      * Returns the time the next execution of this action
      * @return
      */
     public abstract long getExecutionTime();

	@Override
	public int compareTo(AbstractScheduledAction o) {
			Long t1 = getExecutionTime();
			Long t2 = o.getExecutionTime();
				
			return t1.compareTo(t2);
	     }
		

     
     

}
