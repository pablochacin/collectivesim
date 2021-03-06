package collectivesim.events;

import collectivesim.events.Event;
import collectivesim.events.EventObserver;
import collectivesim.dataseries.DataSeries;

/**
 * Generates a DataSeries from the events
 * 
 * @author Pablo Chacin
 *
 */
public class EventSeriesObserver implements EventObserver {

	protected String[] attributes;
	
	protected DataSeries series;
	
	public EventSeriesObserver(DataSeries series, String[] attributes){
		this.series = series;
		this.attributes = attributes;
	}
	
	public EventSeriesObserver(DataSeries series){
		
		this(series, new String[0]);
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void notify(Event event) {
	
		//TODO filter the attributes that are copied to the
		//data item
		series.addItem(event.getAttributes());

	}

}
