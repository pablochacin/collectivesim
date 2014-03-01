package collectivesim.events;

import collectivesim.events.Event;
import collectivesim.events.EventObserver;

/**
 * 
 * Filters events to an observers.
 * 
 * @author Pablo Chacin
 *
 */
public class FilteringEventObserver implements EventObserver {

	/**
	 * List of event prefixes
	 */
	EventFilter filter;
	
	/**
	 * EventObserver to delegate events
	 */
	EventObserver observer;
	
	
	
	public FilteringEventObserver(EventFilter filter, EventObserver observer) {
		this.filter = filter;
		this.observer = observer;
	}



	/**
	 * Checks if the event matches any of the prefixes
	 */
	public void notify(Event event) {
		if(filter.filter(event)){
			observer.notify(event);
		}
		
	}

}
