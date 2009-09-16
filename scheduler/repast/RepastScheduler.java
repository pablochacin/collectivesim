/*
 * Model.java
 * 
 * 
 * (c) Pablo Chacin 2006
 */
package edu.upc.cnds.collectivesim.scheduler.repast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import uchicago.src.sim.engine.BasicAction;
import uchicago.src.sim.engine.Schedule;
import edu.upc.cnds.collectivesim.scheduler.ScheduledAction;
import edu.upc.cnds.collectivesim.scheduler.base.AbstractScheduler;
import edu.upc.cnds.collectivesim.stream.Stream;


/**
 * 
 * Offers the interface to control the simulation.
 * 
 * @author Pablo Chacin <br>
 */
public class RepastScheduler extends AbstractScheduler {


	/**
	 * Repast's model Simulation schedule, used to schedule actions.
	 * must be defined here due to restrictions in Repast's architecture.
	 */
	private Schedule schedule;


	private double nextTime = 0;

	
	/**
	 * List of actions pending for cancellation 
	 */
	private List<BasicAction> cancelQueue;

	
	public RepastScheduler(long speed,long endTime){

		super(speed,endTime);
		
		this.schedule = new Schedule(1);
		this.cancelQueue = new ArrayList<BasicAction>();
		
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


	public synchronized ScheduledAction scheduleRepetitiveAction(Runnable target,int iterations,Stream<Long> frequency, long delay, long endTime) {

		
		double initTime = delay;
		if(initTime == 0){
			initTime = (double)frequency.getValue();
		}
	
		nextTime = Math.min(nextTime, initTime);

		RepetitiveAction action = new RepetitiveAction(this,target,initTime,frequency,iterations, new Double(endTime));
		
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



	/**
	 * TODO: there is no way to stop the Repast scheduler!
	 *       I suspect that this is done automatically when there
	 *       are no more pending actions in the queue? 
	 */
	public void reset() {
				
		//clear the queue of pending cancelled tasks
		cancelQueue.clear();
		
		//Reset repast scheduler
		//TODO: as the repast scheduler don't allow to reset, create a new one
		schedule = new Schedule(1);
	}


	/**
	 * 
	 * @return the time of the next action to be scheduled
	 * 
	 * TODO: current implementation doesn't update the current time after an action is removed from the
	 * scheduler. Therefore, if first action in the queue is removed, the next one will be scheduled at
	 * an improper time (the time the first one should be scheduled)
	 */
	protected double getNextTime() {
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


	@Override
	public void setTerminationTask(Runnable task) {
		throw new UnsupportedOperationException();
		
	}


	protected void executeTasks(){
		
		schedule.execute();
		
		//check for cancelled actions
		//must be done here to avoid concurrent modification of
		//the scheduler's action queue
		Iterator<BasicAction> iter = cancelQueue.iterator();
		while(iter.hasNext()){
			schedule.removeAction(iter.next());
			iter.remove();
		}
	}
	
	public static void main(String[] args){
		
		RepastScheduler sch = new RepastScheduler(0,0);
		
		sch.scheduleAction(new Runnable(){
			public void run(){
				System.out.println("TASK");
			}
		}, 10);
		
		sch.scheduleAction(new Runnable(){
			public void run(){
				System.out.println("TASK");
			}
		}, 20);
		
		sch.setTerminationTask(new Runnable(){
			public void run(){
				System.out.println("END TASK");
			}
		});
		
		sch.start();
	}
}



