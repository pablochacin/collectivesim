/*
 * Model.java
 * 
 * 
 * (c) Pablo Chacin 2006
 */
package edu.upc.cnds.collectivesim.scheduler.repast;

import uchicago.src.sim.engine.Schedule;
import edu.upc.cnds.collectivesim.scheduler.ScheduledAction;
import edu.upc.cnds.collectivesim.scheduler.Scheduler;
import edu.upc.cnds.collectivesim.scheduler.Stream;


/**
 * 
 * Offers the interface to control the simulation.
 * 
 * @author Pablo Chacin <br>
 */
public class RepastScheduler implements Scheduler {

/**	
 * Thread that controls the execution of the actions 
 */
private class SimulationThread implements Runnable {
	
	
	
	@SuppressWarnings("static-access")
	public void run() {
		while(true) {
		  schedule.execute();
		  
		  try {
			  long delay = Math.max((long)(getNextTime()-schedule.getCurrentTime()),1); 
			Thread.currentThread().sleep(delay*speed);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
}
	
 /**
  * Repast's model Simulation schedule, used to schedule actions.
  * must be defined here due to restrictions in Repast's architecture.
  */
  private Schedule schedule;

 /**
  * Speed of the simulation clock
  */
  private long speed;

  private static long DEFAULT_SPEED = 1;
  
  private double nextTime = 0;
  
  
  public RepastScheduler() {
	  this(DEFAULT_SPEED);
  }
  
  public RepastScheduler(long speed) {
	    this.speed = speed;
	    schedule = new Schedule(1);
	    
	    //Start the thread that will control the execution of actions
	    new Thread(new SimulationThread()).start();
  }

  /**
   * @see simrealms.models.Schedule#scheduleAction(simrealms.models.Action)
   */
  public synchronized ScheduledAction scheduleAction(Runnable target, long delay) {
      
	  SingleAction action = new SingleAction(schedule,target,delay);
	  
      nextTime = Math.min(nextTime, action.getNextTime());
            
      schedule.scheduleActionAt(delay,action);
      
      return action;
  }
  
  
  private synchronized ScheduledAction scheduleRepetitiveAction(Runnable target,Stream distribution,long iterations, long endTime) {
	  
	  RepetitiveAction action = new RepetitiveAction(schedule,target,distribution,iterations, new Double(endTime));

      nextTime = Math.min(nextTime, action.getNextTime());

      schedule.scheduleActionAtInterval((Double)distribution.getValue(), action);


      return action;
  }

  
  public ScheduledAction scheduleRepetitiveAction(Runnable target,Stream distribution, int iterations) {
		  return scheduleRepetitiveAction(target, distribution, iterations,0);
	}

  
   public ScheduledAction scheduleRepetitiveAction(Runnable target, Stream distribution, long endTime) {
	  
	  return scheduleRepetitiveAction(target, distribution, 0,endTime);
	  
	}

	public ScheduledAction scheduleRepetitiveAction(Runnable target,Stream distribution) {
		  return scheduleRepetitiveAction(target, distribution, 0,0);
	}

/**
  * @see simrealms.models.ModelInterface#getTime()
  */
public Double getTime(){
    return new Double(schedule.getCurrentTime());
}

public void pause() {
	 throw new UnsupportedOperationException();	
}

public void resume() {
	 throw new UnsupportedOperationException();	
}

public void start() {
	 throw new UnsupportedOperationException();	
}

public void stop() {
 throw new UnsupportedOperationException();
	
}

public long getSpeed() {
	return speed;
}

public void setSpeed(long speed) {
	this.speed = speed;
	
}


/**
 * 
 * @return the time of the next action to be scheduled
 * 
 * TODO: current implementation doesn't update the current time after an action is removed from the
 * scheduler. Therefore, if first action in the queue is removed, the next one will be scheduled at
 * an improper time (the time the first one should be scheduled)
 */
private double getNextTime() {
	return nextTime;
}



}



