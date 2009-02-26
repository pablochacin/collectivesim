package edu.upc.cnds.collectivesim.scheduler.repast;


public class SingleAction extends AbstractScheduledAction {	
	
	/**
	 * object on which the action will be executed
	 */
	private Runnable target;
	

	
	/**
	 * Deleay between executions of the action
	 */
	private long delay;

	/**
	 * Constructor with all parameters
	 * 
	 * @param target an Object on which the action will be executed
	 * @param delay time between executions
	 */
	public SingleAction(RepastScheduler scheduler,Runnable target,long delay) {
		super(scheduler);
		this.target = target;
        this.delay = delay;
        
	}
	
    
	/**
	 * return frequency
	 */
	public double getDelay(){
		return this.delay;
	}
	     
	/**
	 * Return target object.
	 * 
	 */
     public Object getTarget(){
    	 return this.target;
     }
          
     
     
     /**
      * Execute's the action
      *
      */
     public void execute() {
    	 
    	 target.run();
     }
}
