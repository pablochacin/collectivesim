package collectivesim.scheduler.base;

import java.util.logging.Logger;

import collectivesim.scheduler.ScheduledAction;



public abstract class AbstractScheduledAction implements ScheduledAction, Runnable, Comparable<AbstractScheduledAction> {

	
	protected static Logger log = Logger.getLogger("collectivesim.scheduler.base");
	
	
	protected AbstractScheduler scheduler;
	
	/**
	 * object on which the action will be executed
	 */
	protected Runnable target;
	
	protected int priority;
	
	public AbstractScheduledAction(AbstractScheduler scheduler,Runnable target,int priority) {
		this.scheduler = scheduler;
		this.target = target;
		this.priority = priority;
	}

	public AbstractScheduledAction(AbstractScheduler scheduler,Runnable target) {
		this.scheduler = scheduler;
		this.target = target;
		this.priority = 0;
	}

	
	public void run(){
   	 	log.finest("[" + scheduler.getTime() + "]" + target.getClass().getName());
		doExecute();
	}
	
	
	public abstract void doExecute();
	
    /* (non-Javadoc)
	 * @see collectivesim.models.imp.ScheduledAction#cancel()
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
     
     public int getPriority() {
    	 return priority;
     }

	@Override
	public int compareTo(AbstractScheduledAction o) {
			Long t1 = getExecutionTime();
			Long t2 = o.getExecutionTime();
			
			if(t1.equals(t2)) {
				Integer p1 = getPriority();
				Integer p2 = o.getPriority();
				return p1.compareTo(p2);
			}
			
			return t1.compareTo(t2);
	     }
		

     
     

}
