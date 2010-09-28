package edu.upc.cnds.collectivesim.scheduler.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import edu.upc.cnds.collectivesim.scheduler.ScheduledAction;
import edu.upc.cnds.collectivesim.scheduler.Scheduler;

/**
 * Implements a simple event based scheduler.
 * This implementation doesn't pretend to be scalable, but emphasizes simplicity.
 * 
 * @author Pablo Chacin
 *
 */
public class BasicScheduler extends AbstractScheduler {

	private static class FixedElementEmumeation<T> implements Enumeration<T> {

		T value;
		
		public FixedElementEmumeation(T value){
			this.value = value;
		}
		
		@Override
		public boolean hasMoreElements() {
			return false;
		}

		@Override
		public T nextElement() {
			return value;
		}
		
	}

	/**
	 * Maintains the current simulation time
	 */
	protected long clock;
	
	/**
	 * Time for the next simulation round
	 */
	protected long nextTime;
	
	
	protected Queue<AbstractScheduledAction>actions;

		
	public BasicScheduler(long speed, long endTime) {
		super(speed, endTime);

		this.clock = 0;
		
		this.actions = new PriorityQueue<AbstractScheduledAction>();

	}
	
	@Override
	protected List<? extends Runnable>  getReadyTasks() {
				
		clock = getNextTime();
		
		List<AbstractScheduledAction>ready= new ArrayList<AbstractScheduledAction>();

		Iterator<AbstractScheduledAction> i = actions.iterator();
		
		while(i.hasNext()){
			AbstractScheduledAction a = i.next(); 
			if(a.getExecutionTime()<= clock){
				ready.add(a);
				i.remove();
			}
			else{
				break;
			}
		}
		
		Collections.sort(ready);
		
		return ready;
	}

	@Override
	public long getTime() {
		return clock;
	}

	
	@Override
	public void clearActions() {		
		clock = 0;
		actions.clear();
		
	}


	@Override
	public ScheduledAction scheduleAction(Runnable target, int iterations, Enumeration<Long> frequency, long delay, long endTime,int priority) {

		
		long initTime = delay;
		if(initTime == 0){
			initTime = getTime() + frequency.nextElement();
		}
	
		AbstractScheduledAction action = new RepetitiveAction(this,target,initTime,frequency,iterations, endTime,priority);
		
		addAction(action);

		return action;
		
	}
	

	public ScheduledAction scheduleAction(Runnable target, int iterations, Long frequency, long delay, long endTime,int priority) {
		return scheduleAction(target,iterations,new FixedElementEmumeation(frequency),delay,endTime,priority);
	}
	
	public void addAction(AbstractScheduledAction action) {
		
		nextTime = (long) Math.min(nextTime, action.getExecutionTime());
		
		actions.add(action);
		
	}

	@Override
	void cancelAction(AbstractScheduledAction action) {
		actions.remove(action);		
	}

	@Override
	protected long getNextTime() {
		if(actions.isEmpty()){
			throw new IllegalStateException();
		}
		
		return actions.element().getExecutionTime();
	}

	

	public static void main(String[] args){
		
		final Scheduler sch = new BasicScheduler(0,0);
		
		
		sch.scheduleAction(new Runnable(){
			public void run(){
				System.out.println("TASK 10 at " + sch.getTime());
			}
		}, 1,(long)0,(long)10,(long)0,(int)0);
		
		sch.scheduleAction(new Runnable(){
			public void run(){
				System.out.println("Reperitive TASK  at " + sch.getTime());
			}
		}, 0,(long)5,(long)0,(long)200,Integer.MIN_VALUE);
		
		sch.scheduleAction(new Runnable(){
			public void run(){
				System.out.println("TASK 20 at " + sch.getTime());
			}
		}, 1,(long)0,(long)20,(long)0,(int)0);
		
		sch.start();
	}

}
