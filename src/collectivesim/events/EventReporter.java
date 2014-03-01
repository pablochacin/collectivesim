package collectivesim.events;



/**
 * Allows the reporting of events
 * 
 * @author Pablo Chacin
 *
 */
public interface EventReporter {

         
    /**
     * Sets a filter to select events that will be reported
     */
    
    public void setFilter(EventFilter filter);
   
    
   
    /**
     * Fires the given event
     * 
     * @param event
     */
    public void fireEvent(Event event);
}