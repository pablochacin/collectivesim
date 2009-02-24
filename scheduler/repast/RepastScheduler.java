/*
 * Model.java
 * 
 * 
 * (c) Pablo Chacin 2006
 */
package edu.upc.cnds.collectivesim.scheduler.repast;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import uchicago.src.sim.engine.Schedule;
import edu.upc.cnds.collectivesim.model.Stream;
import edu.upc.cnds.collectivesim.scheduler.ScheduledAction;
import edu.upc.cnds.collectivesim.scheduler.Scheduler;


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

	private static boolean DEFAULT_PAUSED = false;

	private double nextTime = 0;

	private boolean paused;
	
	private long endTime;

	/**
	 * Lock used to control the execution of the update thread
	 */
	private Lock updateLock;
	
	private Condition pausedCondition;

	public RepastScheduler() {
		this(DEFAULT_SPEED,DEFAULT_PAUSED,0);
	}

	public RepastScheduler(long speed,boolean paused,long endTime){

		this.speed = speed;
		this.paused = paused;
		this.endTime = endTime;
		this.updateLock = new ReentrantLock();
		this.pausedCondition = updateLock.newCondition();
		this.schedule = new Schedule(1);

		//Start the thread that will control the execution of actions
		new Thread(new SimulationThread()).start();

	}

	public RepastScheduler(long speed) {
		this(speed,DEFAULT_PAUSED,0);
	}

	/**
	 * @see simrealms.models.Schedule#scheduleAction(simrealms.models.Action)
	 */
	public synchronized ScheduledAction scheduleAction(Runnable target, long delay) {

		SingleAction action = new SingleAction(schedule,target,delay);

		nextTime = Math.min(nextTime, action.getNextTime());

		schedule.scheduleActionAt(schedule.getCurrentTime()+delay,action);

		return action;
	}


	public synchronized ScheduledAction scheduleRepetitiveAction(Runnable target,int iterations,Stream<Long> distribution, long delay, long endTime) {

		RepetitiveAction action = new RepetitiveAction(schedule,target,distribution,iterations, new Double(endTime));

	
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
	public Double getTime(){
		return new Double(schedule.getCurrentTime());
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

	public void reset() {
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



