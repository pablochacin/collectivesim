package collectivesim.events;

/**
 * Controls the execution of a monitoring compomentent
 * 
 * @author Pablo Chacin
 *
 */
public interface Monitor {

    /**
     * start reporting
     */
    public void start();
    
    /**
     * Stop reporting and reset.
     *
     */
    public void stop();
    
    
    /**
     * Pause reporting. Events received while paused are lost.
     */
    public void pause();
    
    
    /**
     * Resume reporting
     *
     */
    public void resume();
    
    /**
     * Sets the reporting frequency
     * 
     * @param frequency the delay, in milseconds between reporting events.
     * 
     */
    public void setFrequency(long frequency);
	
}
