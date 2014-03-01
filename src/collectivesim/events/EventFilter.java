package collectivesim.events;

public interface EventFilter {
    
    /**
     * Filters events
     * 
     * @param event
     * 
     * @return true if the event passes the filter
     */
    public boolean filter(Event event);
}
