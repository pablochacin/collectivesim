package edu.upc.cnds.collectivesim.scheduler.repast;


public class SingleAction extends AbstractScheduledAction {	
	
	
	/**
	 * Delay between executions of the action
	 */
	private long delay;

	/**
	 * Constructor with all parameters
	 * 
	 * @param target an Object on which the action will be executed
	 * @param delay time between executions
	 */
	public SingleAction(RepastScheduler scheduler,Runnable target,long delay) {
		super(scheduler,target);
        this.delay = delay;
        setNextTime(delay);
        
	}
	
    
	/**
	 * return frequency
	 */
	public double getDelay(){
		return this.delay;
	}
	               
     
     
     /**
      * Execute's the action
      *
      */
     public void doExecute() {
    	 
    	 target.run();
     }
}
