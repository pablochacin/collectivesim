package edu.upc.cnds.collectivesim.model.base;

import java.util.logging.Logger;

import edu.upc.cnds.collectivesim.model.Model;
import edu.upc.cnds.collectivesim.stream.Stream;

/**
 * An repetitive action that is executed in a Model
 * 
 * @author Pablo Chacin
 *
 */
public abstract class ModelAction implements Runnable {

	protected static Logger log = Logger.getLogger("collectivesim.model");

	/**
	 * Number of iterations of the behavior
	 */
	protected int iterations;

	/**
	 * Delay between successive iterations
	 */
	protected Stream<Long> frequency;

	/**
	 * Delay before the first iteration
	 */
	protected long delay; 

	/**
	 * End time
	 */
	protected long endTime;

	/**
	 * Indicates if the behavior is active
	 */
	protected boolean active=false;
	
	
	protected int priority;
	
	protected Model model;
	
	protected String name;

	public ModelAction(Model model,String name,boolean active,int iterations, Stream<Long> frequency, long delay,long endTime,int priority) {
		this.model = model;
		this.name = name;
		this.active = active;
		this.iterations = iterations;
		this.frequency = frequency;
		this.delay = delay;
		this.endTime = endTime;
		this.priority = priority;
	}

	public int getIterations() {
		return iterations;
	}

	public Stream<Long> getFrequency() {
		return frequency;
	}

	public long getDelay() {
		return delay;
	}

	public long getEndTime() {
		return endTime;
	}

	public int getPriority() {
		return priority;
	}
	
	/**
	 * starts the execution of the behavior 
	 */	
	public void start(){
		active = true;
	}

	/**
	 * pause the execution of the behavior
	 */
	public void pause(){
		active = false;
	}
	
	
	public void resume(){
		active = true;
	}

	public void run(){
		if(!active) {
			return;
		}

		if(model.isDebugging()){
			System.out.println("[" + model.getCurrentTime() + "] [" + getType() + "][" + name + "]");
		}
		
		execute();
	}
	
	
	public Model getModel(){
		return model;
	}
	
	public String getName(){
		return name;
	}
	
	/**
	 * 
	 * @return the type of action
	 */
	protected abstract String getType();
	
	/**
	 * Execute the action's logic.
	 */
	protected abstract void execute();
	
	
}
