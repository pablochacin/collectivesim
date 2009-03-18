package edu.upc.cnds.collectivesim.experiment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.sun.corba.se.spi.legacy.connection.GetEndPointInfoAgainException;

import edu.upc.cnds.collectives.events.Event;
import edu.upc.cnds.collectives.events.EventObserver;
import edu.upc.cnds.collectives.events.imp.FilteringObserver;
import edu.upc.cnds.collectives.util.FormattingUtils;
import edu.upc.cnds.collectives.util.TypedMap;
import edu.upc.cnds.collectivesim.dataseries.DataSeries;
import edu.upc.cnds.collectivesim.dataseries.baseImp.BaseDataSeries;
import edu.upc.cnds.collectivesim.model.Model;
import edu.upc.cnds.collectivesim.model.ModelException;
import edu.upc.cnds.collectivesim.model.SingleValueStream;
import edu.upc.cnds.collectivesim.model.Stream;
import edu.upc.cnds.collectivesim.scheduler.Scheduler;

/**
 * Represents a simulation experiment. Supports the execution of Models, and hosts
 * the elements used by them like counters, data series, parameters. Also allows the
 * installation of diverse observers to follow experiment's results.
 * 
 * More concretely it defines the following elements
 * <ul>
 * <li>Parameters
 * <li>Streams
 * <li>DataSeries
 * used by Models and hosts the Counters generated by models' execution. 
 * </ul>
 * Also provides
 * new DataSeries from the values of experiment counters. 
 * 
 * The proper order for launching an experiment is:
 * <ol>
 * <li> create scheduler (needed by the Experiment)
 * <li> set up parameters
 * <li> set up tables
 * <li> create DataSeries
 * <li> create Streams
 * <li> create counters
 * <li> create models (need all the above)
 * <li> create counter observers (need counters)
 * <li> add Plots (needs data series)
 * <li> add event observers
 * <li> start experiments (starts scheduler and models)
 * </ol>
 * @author Pablo Chacin
 *
 */
public class Experiment {

	private class ExperimentTermination implements Runnable{
		public void run(){
			for(Runnable r: endTasks){
				r.run();
			}
		}
	}
	
	private Logger log;
	
	private Map<String, Model> models;
	
	private Map<String,Stream> streams;
	
	private Map<String,Counter>counters;
	
	private Map<String,DataSeries>series;
	
	private Map<String,Table>tables;
	
	private List<EventObserver>observers;
	
	private TypedMap parameters;
	
	private Scheduler scheduler;
		
	private String description;
	
	private List<Runnable> initTasks;
	
	private List<Runnable>endTasks;
	
	public Experiment(String description,Scheduler scheduler,long endTime){
		
		this.log = Logger.getLogger("colectivesim.experiment");
		
		this.description = description;
		this.scheduler = scheduler;
		this.models = new HashMap<String,Model>();
		this.counters = new HashMap<String, Counter>();
		this.tables = new HashMap<String, Table>();
		this.series = new HashMap<String, DataSeries>();
		this.streams = new HashMap<String, Stream>();
		this.parameters = new TypedMap();
		this.observers = new ArrayList<EventObserver>();
		this.initTasks = new ArrayList<Runnable>();
		this.endTasks = new ArrayList<Runnable>();
		
		//If the experiment has an end time, schedule its termination
		if(endTime != 0){
			scheduler.scheduleAction(new Runnable(){
				public void run(){
					stop();
				}
			}, endTime);
		}
	}
	
	
	public String getDescription(){
		return description;
	}
	
	/**
	 * @param name of the stream
	 * @return a Stream with the given name or null, if none exists
	 */
	public Stream getStream(String name){
		return streams.get(name);
	}

	/**
	 * Adds a Stream to the Experiment
	 * @param name
	 * @param stream
	 */
	public void addStream(Stream stream){
		streams.put(stream.getName(), stream);
	}
	
	
	public Counter addCounter(String name){
	
		Counter counter = new Counter(name);
		counters.put(name, counter);
		return counter;
	}

	
	public Counter getCounter(String name){
		return counters.get(name);
	}
	
	/**
	 * Adds a table of values to the experiment
	 * @param name
	 * @param values
	 * @throws ExperimentException 
	 */
	
	public void addTable(Table table) throws ExperimentException{
	
		tables.put(table.getName(),table);
		table.load();
	}
	
	public Table getTable(String name){
		return tables.get(name);
	}
	
	
	/**
	 * Created a default dataseries implementation
	 * @param name
	 * @return
	 */
	public DataSeries addDataSeries(String name){
		DataSeries dataseries = new BaseDataSeries(name);
		series.put(name, dataseries);
		return dataseries;
	}
	

	/**
	 * Adds an existing DataSeries to the model
	 * @param dataseries
	 * @return
	 */
	public DataSeries addDataSeries(DataSeries dataseries){
		series.put(dataseries.getName(), dataseries);
		return dataseries;
	}

	/**
	 * 
	 * @param name
	 * 
	 * @return the DataSeries registered under the given name or null if none exists
	 */
	public DataSeries getDataSeries(String name) {
		return series.get(name);
	}
	
	/**
	 * Generates a Dataseries by observing the counter's value periodically.
	 * 
	 * @param counter
	 * @param dataSeries
	 * @param frequency
	 */
	public void addCounterObserver(String counter,String dataSeries,long frequency){
	
		CounterObserverAction observer = new CounterObserverAction(getCounter(counter),
				                                                   getDataSeries(dataSeries));
		
		scheduler.scheduleRepetitiveAction(observer, new SingleValueStream<Long>("",new Long(frequency)));
	}


	/**
	 * Generates a DataSeries by calculating periodically a function with the values from some
	 * counters.
	 * 
	 * Function's <code>calculate</code> method is called on each update cycle passing the value of 
	 * the counters in the given order. If the number of counters specified is different from 
	 * those expected by the Function, the function might throw a InvalidArgumentException.
	 * 
	 * @param counters
	 * @param function
	 * @param frequency
	 */
	public void addCalculatingObserver(String[] counterNames,Function function,String dataSeries,long frequency){
		
		Counter[] counters = new Counter[counterNames.length];
		for(int i=0;i<counters.length;i++){
			counters[i] = getCounter(counterNames[i]);
		}
		
		FunctionCalculatorCounterObserver observer = new FunctionCalculatorCounterObserver(counters, getDataSeries(dataSeries), function);

		scheduler.scheduleRepetitiveAction(observer, new SingleValueStream<Long>("",new Long(frequency)));

	}
	
	public void start() throws ExperimentException{
		
		//execute initiation tasks
		for(Runnable r: initTasks){
			r.run();
		}
		
		//initiate schedulling of events
		scheduler.start();
	}
	/**
	 * Pauses the execution of the experiment and all its models and observers
	 */
	public void pause(){
		throw new UnsupportedOperationException();	
	}
	
	/**
	 * Resumes the execution of the experiment
	 */
	public void resume(){
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Ends the execution of the experiment
	 */
	public void stop(){
		
		//execute termination tasks
		for(Runnable r: endTasks){
			r.run();
		}

	}
	
	/**
	 * Sets the time at which the experiment will be paused
	 * 
	 * @param time
	 */
	public void setEndTime(long time){
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Adds a task whose run() method will be invoked before the experiment is started
	 * 
	 * There is no guarantee that tasks will be executed in the same order than they were added
	 * and actual implementation might run them in sequence or parallel.
	 * 
	 * @param task
	 */
	public void addInitilizationTask(Runnable task){
		initTasks.add(task);
	}
	
	/**
	 * Adds a task whose run() method will be invoked before the experiment is stopped.
	 * 
	 * There is no guarantee that tasks will be executed in the same order than they were added
	 * and actual implementation might run them in sequence or parallel.
	 *  
	 * @param task
	 */
	public void addFinalizationTask(Runnable task){
		endTasks.add(task);
	}
	
	/**
	 * Adds a task whose run() method will be invoked at the given frequency 
	 * 
	 * @param task
	 */
	public void addPeriodicTask(Runnable task,long delay, long frequency){
		scheduler.scheduleRepetitiveAction(task,0,new SingleValueStream<Long>("", frequency),delay,0);
	}
	
	public TypedMap getParameters(){
		return parameters;
	}
	
	
	/**
	 * 
	 * @return the Scheduler that controls the experiment's execution
	 */
	public Scheduler getScheduler(){
		return scheduler;
	}
	
	/**
	 * Adds a model to the experiment
	 * @param model
	 * @param delay time at which the model must be started
	 */
	public void addModel(final Model model,long delay){
		models.put(model.getName(),model);
		
		//create an anonymous Runnable to execute the start of the model
		Runnable target = new Runnable(){
			    public void run(){
			    	try {
			    		model.start();
			    	} catch (ModelException e) {
			    		log.severe("Exception starting mode "+model.getName()+FormattingUtils.getStackTrace(e));
			    	}}};
		
		scheduler.scheduleAction(target, delay);
		
	}
	

	/**
	 * Adds a model to the experiment to be started at time 0
	 * @param model
	 */
	public void addModel(Model model){
		addModel(model,0);
	}

	
	public Model getModel(String name){
		return models.get(name);
	}
	
	/**
	 * Adds an observer for any type of event
	 * 
	 * @param observer
	 */
	public void registerEventObserver(EventObserver observer){
		observers.add(observer);
	}
	
	/**
	 * Adds an observer for an specific type of events
	 * 
	 * @param observer
	 * @param events
	 */
	public void registerEventObserver(EventObserver observer,String[] events){
		observers.add(new FilteringObserver(events,observer));
	}

	/**
	 * Reports an event from a Model
	 * @param event
	 */
	public void reportEvent(Event event) {
		for(EventObserver o: observers){
			o.notify(event);
		}
		
	}

}
