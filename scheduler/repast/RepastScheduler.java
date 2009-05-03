/*
 * Model.java
 * 
 * 
 * (c) Pablo Chacin 2006
 */
package edu.upc.cnds.collectivesim.scheduler.repast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import uchicago.src.sim.engine.BasicAction;
import uchicago.src.sim.engine.Schedule;
import edu.upc.cnds.collectivesim.scheduler.ScheduledAction;
import edu.upc.cnds.collectivesim.scheduler.Scheduler;
import edu.upc.cnds.collectivesim.stream.Stream;


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
				//execute the update. Use the updateLock to wait 
				//if the thread is paused
				updateLock.lock();
				schedule.execute();
				
				//check for cancelled actions
				//must be done here to avoid concurrent modification of
				//the scheduler's action queue
				for(BasicAction a: cancelQueue){
					schedule.removeAction(a);
				}
				if((endTime != 0) && (getTime() >= endTime)){
					paused = true;
				}
							
				
				try {
				
					if(paused){
						pausedCondition.await();
					}

					updateLock.unlock();

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

	private boolean paused;
	
	private long endTime;

	/**
	 * Lock used to control the execution of the update thread
	 */
	private Lock updateLock;
	
	private Condition pausedCondition;
	
	/**
	 * List of actions pending for cancellation 
	 */
	private List<BasicAction> cancelQueue;

	public RepastScheduler() {
		this(DEFAULT_SPEED,0);
	}

	public RepastScheduler(long speed,long endTime){

		this.speed = speed;
		this.paused = true;
		this.endTime = endTime;
		this.nextTime = 0;
		this.updateLock = new ReentrantLock();
		this.pausedCondition = updateLock.newCondition();
		this.schedule = new Schedule(1);
		this.cancelQueue = new ArrayList<BasicAction>();
		
		//Start the thread that will control the execution of actions
		new Thread(new SimulationThread()).start();
	}

	/**
	 * Start dispatching events
	 */
	public void start(){
		
		resume();

	}
	
	public RepastScheduler(long speed) {
		this(speed,0);
	}

	/**
	 * @see simrealms.models.Schedule#scheduleAction(simrealms.models.Action)
	 */
	public synchronized ScheduledAction scheduleAction(Runnable target, long delay) {

		SingleAction action = new SingleAction(this,target,delay);

		nextTime = Math.min(nextTime, action.getNextTime());

		schedule.scheduleActionAt(schedule.getCurrentTime()+delay,action);

		return action;
	}


	public synchronized ScheduledAction scheduleRepetitiveAction(Runnable target,int iterations,Stream<Long> distribution, long delay, long endTime) {

		RepetitiveAction action = new RepetitiveAction(this,target,distribution,iterations, new Double(endTime));

	
		double initTime = delay;
		if(initTime == 0){
			initTime = (double)distribution.getValue();
		}
	
		nextTime = Math.min(nextTime, initTime);
		
		schedule.scheduleActionAtInterval(initTime, action);

		return action;
	}


	public ScheduledAction scheduleRepetitiveAction(Runnable target, Stream<Long> frequency){
		return scheduleRepetitiveAction(target,0,frequency,0,0);
	}


	/**
	 * @see simrealms.models.ModelInterface#getTime()
	 */
	public long getTime(){
		return new Double(schedule.getCurrentTime()).longValue();
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

	/**
	 * TODO: there is no way to stop the Repast scheduler!
	 *       I suspect that this is done automatically when there
	 *       are no more pending actions in the queue? 
	 */
	public void reset() {
		
		//stop the dispatch thread
		pause();	
		
		//clear the queue of pending cancelled tasks
		cancelQueue.clear();
		
		//Reset repast scheduler
		//TODO: as the repast scheduler don't allow to reset, create a new one
		schedule = new Schedule(1);
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

	/**
	 * Adds the given action to the queue of canceled actions. 
	 * Before next cycle, all canceled actions will be removed from the
	 * Repast's scheduler. 
	 * 
	 * @param action
	 */
	 void cancelAction(AbstractScheduledAction action) {
		cancelQueue.add(action);
	}





}



