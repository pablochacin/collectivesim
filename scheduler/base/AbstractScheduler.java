package edu.upc.cnds.collectivesim.scheduler.base;



import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import edu.upc.cnds.collectivesim.scheduler.Scheduler;


/**
 * 
 * Offers the interface to control the simulation.
 * 
 * @author Pablo Chacin <br>
 */
public abstract class AbstractScheduler implements Scheduler {

	/**	
	 * Thread that controls the execution of the actions 
	 */
	private class SimulationThread implements Runnable {


		/**
		 * Dispatches the task for the current simulation interval
		 */
		public void run() {
			while(true) {
				boolean terminated = false;
				
				
				List<? extends Runnable> tasks = getReadyTasks();
				for(Runnable r: tasks){
					r.run();
				}
								
				if((endTime != 0) && (getTime() >= endTime)){
					terminated = true;
				}
				
				if(tasks.isEmpty()){
					terminated = true;
				}
				
				if(terminated){
					terminated();
					return;
				}
				
				try {
				
					if(paused){
						pausedCondition.await();
					}


					long delay = Math.max((long)(getNextTime()- getTime()),1);					
					Thread.currentThread().sleep(delay*speed);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}


	/**
	 * Speed of the simulation clock
	 */
	private long speed;

	private static long DEFAULT_SPEED = 1;

	protected boolean paused;
		
	
	private long endTime;
	
	protected Runnable terminationHandler;

	/**
	 * Lock used to control the execution of the update thread
	 */
	private Lock updateLock;
	
	private Condition pausedCondition;
	
	private Thread schedulerThread;
	
	public AbstractScheduler(long speed,long endTime){

		this.speed = speed;
		this.paused = true;
		this.endTime = endTime;
		this.updateLock = new ReentrantLock();
		this.pausedCondition = updateLock.newCondition();
				
		//initialize with dummy handler
		terminationHandler = new Runnable(){
			public void run(){};
		};

	}

	public void start(){
		start(0);
	}
	
	/**
	 * Start dispatching events
	 */
	public void start(long endTime){
		
		this.endTime = endTime;
		
		resume();

		//Start the thread that will control the execution of actions
		schedulerThread = new Thread(new SimulationThread());
		schedulerThread.start();
	}
	


	public void reset(){
		if(!paused){
			throw new IllegalStateException("Scheduler must be paused");
		}
		
		clearActions();
	}

	
	public void pause() {
		updateLock.lock();
		paused= true;
		updateLock.unlock();
	}

	public void resume() {
		updateLock.lock();
		paused = false;
		pausedCondition.signal();
		updateLock.unlock();
	}


	public long getSpeed() {
		return speed;
	}

	public void setSpeed(long speed) {
		this.speed = speed;

	}



	@Override
	public long getEndTime() {
		return endTime;
	}

	
	@Override
	public void setTerminationHandler(Runnable handler){
		this.terminationHandler = handler;
	}
	
	
	/**
	 * Execute tasks scheduled at current simulation time
	 */
	protected abstract List<? extends Runnable> getReadyTasks();

	
	/**
	 * 
	 * @return the time of the next action to be scheduled
	 * 
	 * TODO: current implementation doesn't update the current time after an action is removed from the
	 * scheduler. Therefore, if first action in the queue is removed, the next one will be scheduled at
	 * an improper time (the time the first one should be scheduled)
	 */
	protected abstract long getNextTime() ;
	
	
	/**
	 * Cancels the action preventing any future execution
	 * 
	 * @param action
	 */
	 abstract void cancelAction(AbstractScheduledAction action);

	 
	 /**
	  * Clear all actions and reset clock
	  */
	 abstract void clearActions();

	 /**
	  * Notifies there are no pending actions 
	  */
	 protected void terminated(){	
		 	
		 	terminationHandler.run();
	 }

}



