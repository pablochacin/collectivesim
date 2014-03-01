package collectivesim.events;

import collectivesim.events.EventFilter;
import collectivesim.events.EventObserver;

/**
 * Collects events from other components
 * 
 * @author Pablo Chacin
 *
 */
public interface EventCollector  {
	
    /**
     * Registers an observer to be notified of the events reported to this Collector
     * 
     * @param observer
     */
    public void registerObserver(EventObserver observer);

    
    /**
     * Register an observer and a filter to select the events to be reported to the observer
     * @param observer
     * @param filter
     */
    public void registerObserver(EventObserver observer, EventFilter filter);
        

    /**
     * Starts collecting events
     */
    public void start();
    
    /**
     * Stop collecting events
     */
    public void stop();
}
