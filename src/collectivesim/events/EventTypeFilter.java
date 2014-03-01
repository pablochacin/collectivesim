package collectivesim.events;

import collectivesim.events.Event;
import collectivesim.events.EventObserver;

/**
 * 
 * Filters events to an observers based on the event's type using regular expressions
 * 
 * @author Pablo Chacin
 *
 */
public class EventTypeFilter implements EventFilter{

	/**
	 * List of event prefixes
	 */
	String[] expresions;

	
	public EventTypeFilter(String[] expresions) {
		this.expresions = expresions;
	}


	/**
	 * Checks if the event matches any of the event selection expressions
	 */
	@Override
	public boolean filter(Event event) {
		for(String e: expresions){
			if(event.getType().matches(e)){;
				return true;
			}
		}	
		
		return false;
	}

}
