package collectivesim.events;

/**
 * Receives notification of events
 * 
 * @author Pablo Chacin
 *
 */
public interface EventObserver {

	/**
	 * Informs the occurence of an event
	 * @param event
	 */
	public void notify(Event event);
	
}
