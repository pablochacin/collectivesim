package edu.upc.cnds.collectivesim.model.base;

import java.util.logging.Logger;

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

	public ModelAction(boolean active,int iterations, Stream<Long> frequency, long delay,long endTime) {
		this.active = active;
		this.iterations = iterations;
		this.frequency = frequency;
		this.delay = delay;
		this.endTime = endTime;
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

		execute();
	}
	
	
	/**
	 * Execute the action's logic.
	 */
	protected abstract void execute();
	
	
}
