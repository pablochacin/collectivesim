package edu.upc.cnds.collectivesim.experiment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;


import edu.upc.cnds.collectives.Collectives;
import edu.upc.cnds.collectives.configuration.Configuration;
import edu.upc.cnds.collectives.events.Event;
import edu.upc.cnds.collectives.events.EventFilter;
import edu.upc.cnds.collectives.events.EventObserver;
import edu.upc.cnds.collectives.events.EventTypeFilter;
import edu.upc.cnds.collectives.events.FilteringEventObserver;
import edu.upc.cnds.collectives.execution.ExecutionService;
import edu.upc.cnds.collectives.execution.Task;
import edu.upc.cnds.collectives.platform.Platform;
import edu.upc.cnds.collectives.util.FileUtils;
import edu.upc.cnds.collectives.util.FormattingUtils;
import edu.upc.cnds.collectives.util.TypedMap;
import edu.upc.cnds.collectivesim.CollectiveSim;
import edu.upc.cnds.collectivesim.dataseries.DataSeries;
import edu.upc.cnds.collectivesim.dataseries.SeriesFunction;
import edu.upc.cnds.collectivesim.dataseries.base.BaseDataSeries;
import edu.upc.cnds.collectivesim.experiment.base.DataSeriesObserverTask;
import edu.upc.cnds.collectivesim.experiment.base.ExperimentTask;
import edu.upc.cnds.collectivesim.model.Model;
import edu.upc.cnds.collectivesim.model.ModelException;
import edu.upc.cnds.collectivesim.scheduler.Scheduler;
import edu.upc.cnds.collectivesim.state.CalculatedValue;
import edu.upc.cnds.collectivesim.state.Counter;
import edu.upc.cnds.collectivesim.state.StateValue;
import edu.upc.cnds.collectivesim.state.StateValueObserver;
import edu.upc.cnds.collectivesim.state.ValueFunction;
import edu.upc.cnds.collectivesim.stream.Stream;
import edu.upc.cnds.collectivesim.stream.StreamException;
import edu.upc.cnds.collectivesim.table.Table;
import edu.upc.cnds.collectivesim.table.TableException;

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
public class Experiment implements Platform, ExecutionService {

	private Logger log;

	private Map<String, Model> models;

	private Map<String,Stream> streams;

	private Map<String,StateValue>stateValues;

	private Map<String,Counter>counters;

	private Map<String,DataSeries>series;

	/**
	 * DataSeries that shouldn't be reset between runs
	 */
	private Set<DataSeries> persistentDS;

	private Map<String,Table>tables;

	private List<EventObserver>observers;

	private TypedMap parameters;

	private Scheduler scheduler;

	private String description;

	/*
	 * Indicates the end of a run
	 */
	private Condition endRunCondition;

	private Lock executionLock;


	/**
	 * Tasks to execute at the beginning of the experiment
	 */
	private List<Runnable> initializationTasks;

	/**
	 * Tasks to execute at the end of the experiment
	 */
	private List<Runnable>terminationTasks;

	/**
	 * Tasks to execute at the begin of each run
	 */
	private List<Runnable>beginRunTasks;

	/**
	 * Tasks to execute at the end of each run
	 */
	private List<Runnable>endRunTasks;


	/**
	 * Tasks to be scheduled for each experiment run.
	 */
	private List<ExperimentTask>scheduledTasks;


	/**
	 * Directory on which result or working files should be created
	 */
	private File rootDir;

	/**
	 * working dir for each run. Same as workingDir is only one run
	 */
	private File runDir;
	
	/**
	 * Working directiry
	 */
	private File workingDir;

	
	/**
	 * start time of the experiment in real time
	 */
	private long beginTime;

	/**
	 * End time of the experiment in real time
	 */
	private long endTime;


	private boolean exitOnEnd;

	/**
	 * Duration of the experiment in simulated time
	 */
	private long runTime;

	/**
	 * length of each run
	 */
	private long runLength;


	/**
	 * Number of runs for the experiment
	 */
	private int runs;
	
	/**
	 * Delay before starting execution, in seconds. Use for scheduled executions;
	 */
	private long delay;
	
	private Random randomGenerator;
	
	public Experiment(String description,Random randomGenerator,Scheduler scheduler,String rootDir, int runs,long runLenght,boolean exitOnEnd,long delay){

		this.log = Logger.getLogger("colectivesim.experiment");

		this.delay = delay;
		this.beginTime = System.currentTimeMillis();
		this.endTime = 0;
		this.runLength = runLenght;
		this.runs = runs;
		
		this.randomGenerator = randomGenerator;

		this.description = description;
		this.scheduler = scheduler;
		this.models = new HashMap<String,Model>();
		this.stateValues = new HashMap<String, StateValue>();
		this.counters = new HashMap<String, Counter>();		
		this.tables = new HashMap<String, Table>();
		this.series = new HashMap<String, DataSeries>();
		this.persistentDS = new HashSet<DataSeries>();
		this.streams = new HashMap<String, Stream>();
		this.parameters = new TypedMap();
		this.observers = new ArrayList<EventObserver>();
		this.initializationTasks = new ArrayList<Runnable>();
		this.terminationTasks = new ArrayList<Runnable>();
		this.beginRunTasks = new ArrayList<Runnable>();
		this.endRunTasks = new ArrayList<Runnable>();
		this.scheduledTasks = new ArrayList<ExperimentTask>();
		this.executionLock = new ReentrantLock();
		this.endRunCondition = executionLock.newCondition();

		this.rootDir = FileUtils.createWorkingDirectory(rootDir);
		this.workingDir = this.rootDir;
		
		this.exitOnEnd = exitOnEnd;

		//If the experiment has an end time, schedule its termination
		if(endTime != 0){

			Runnable endTask = new Runnable(){
				public void run(){
					endRun();
				}
			};

			scheduledTasks.add(new ExperimentTask(scheduler,endTask,endTime));
		}

		Collectives.setPlaform(this);
		CollectiveSim.setExperiment(this);
		
	}



	public long beginTime(){
		return beginTime;
	}

	public long endTime(){
		return endTime;
	}

	public long getRunTime(){
		return runTime;
	}

	public long getRunLength(){
		return runLength;
	}

	public String getDescription(){
		return description;
	}

	public String getRootDirectory(){
		return rootDir.getAbsolutePath();
	}

	/**
	 * Returns the working directory at the moment of the invocation.
	 * While in an experiment run, the working directory is the run's directory
	 * For initialization and termination tasks, the working directory is the experiment's root directory.
	 * 
	 * @return the path to the current working directory 
	 */
	public String getWorkingDirectory(){
		return workingDir.getAbsolutePath();
	}

	/**
	 * 
	 * @return the current real time
	 */
	public Long getRealTime(){
		return System.currentTimeMillis();
	}

	/**
	 * @param name of the stream
	 * @return a Stream with the given name or null, if none exists
	 */
	public Stream getStream(String name){
		Stream stream = streams.get(name);
		if(stream == null)
			throw new IllegalArgumentException("Stream not found " + name);

		return  stream;
	}

	/**
	 * Adds a Stream to the Experiment
	 * @param name
	 * @param stream
	 */
	public void addStream(String name,Stream stream){
		streams.put(name, stream);
	}

	
	/**
	 * Returns a random number generator 
	 * 
	 * @return
	 */
	public Random getRandomGenerator() {
		
		return randomGenerator;
	}

	public Counter addCounter(String name){

		Counter counter = new Counter(name);
		stateValues.put(name, counter);
		counters.put(name,counter);
		return counter;
	}


	public Double getStateValue(String name){
		StateValue value = stateValues.get(name); 
		if(value == null){
			throw new IllegalArgumentException("Value not found: "+name);
		}

		return value.getValue();
	}

	public Counter getCounter(String name){
		Counter counter = counters.get(name); 
		if(counter == null){
			throw new IllegalArgumentException("Counter not found: "+name);
		}

		return counter;
	}


	/**
	 * Adds a new calculated value, whose value is obtained by applying
	 * a function to a list of state values (counters or other calculated values)
	 * 
	 * @param values
	 * @param function
	 */
	public void addCalculatedValue(String name,String[]arguments,ValueFunction function){
		StateValue[] values = new StateValue[arguments.length];
		for(int i=0;i < values.length;i++){
			StateValue value = stateValues.get(arguments[i]);
			if(value == null){
				throw new IllegalArgumentException("State Value "+ arguments[i]+ " not defined");
			}
			values[i] = value;
		}
		stateValues.put(name,new CalculatedValue(name,values,function));
	}

	
	/**
	 * Convenience method, receiving arguments as a string of comma delimited names.
	 * @param name
	 * @param arguments
	 * @param function
	 */
	public void addCalculatedValue(String name,String arguments,ValueFunction function){
		addCalculatedValue(name,arguments.split(","),function);
	}
	
	/**
	 * Adds a table of values to the experiment
	 * @param name
	 * @param values
	 * @throws ExperimentException 
	 */

	public void addTable(Table table) throws ExperimentException{

		tables.put(table.getName(),table);
	}

	public Table getTable(String name){
		return tables.get(name);
	}


	/**
	 * Created a  DataSeries 
	 * @param name of the DataSeries
	 * @param size maximum size. When this size is reached, new items displace older one.
	 * @return the newly created DataSeries
	 */
	public DataSeries addDataSeries(String name,int size,boolean persistent){

		if(series.containsKey(name)){
			throw new IllegalArgumentException("DataSeries " + name + " already exists");
		}

		DataSeries dataseries = new BaseDataSeries(name,size);
		series.put(name, dataseries);

		if(persistent){
			persistentDS.add(dataseries);
		}

		return dataseries;
	}

	public DataSeries addDataSeries(String name,int size){
		return addDataSeries(name,size,false);
	}

	/**
	 * Convenience method, assumes non persistent dataseries
	 * @param name
	 * @return
	 */
	public DataSeries addDataSeries(String name){
		return addDataSeries(name,0);
	}

	/**
	 * 
	 * @param name
	 * 
	 * @return the DataSeries registered under the given name or null if none exists
	 */
	public DataSeries getDataSeries(String name) {

		DataSeries dataseries = series.get(name);

		if(dataseries == null){
			throw new IllegalArgumentException("DataSeries " + name +" is not valid");
		}

		return dataseries;
	}

	/**
	 * Creates an observer that periodically applies a SeriesFunction to a DataSeries and puts the
	 * result in another DataSeries.
	 * 
	 * @param series
	 * @param function
	 * @param result
	 * @param frequency
	 */
	public void addDataSeriesObserver(String target, SeriesFunction function, String result,long frequency,long delay){

		DataSeries targetSeries = getDataSeries(target);

		DataSeries resultSeries = getDataSeries(result);

		DataSeriesObserverTask observer = new DataSeriesObserverTask(targetSeries,function,resultSeries,false,false);

		scheduledTasks.add(new ExperimentTask(scheduler, observer,delay,frequency,0));
	}

	/**
	 * Adds an observer that applies a SeriesFunction to a target DataSeries each time it is 
	 * updated and produces a result DataSeries.
	 * 
	 * @param target
	 * @param function
	 * @param result
	 */
	public void addDataSeriesObserver(String target, SeriesFunction function, String result){

		DataSeries targetSeries = getDataSeries(target);

		DataSeries resultSeries = getDataSeries(result);

		DataSeriesObserverTask observer = new DataSeriesObserverTask(targetSeries,function,resultSeries,true,false);

		//targetSeries.addObserver(observer);
	}	

	/**
	 * Generates a DataSeries by observing a StateValue periodically.
	 * 
	 * @param name
	 * @param dataSeries
	 * @param frequency
	 */
	public void addStateObserver(String name,String dataSeries,long frequency,long delay,boolean incremental){

		StateValue value = stateValues.get(name);
		if(value == null){
			throw new IllegalArgumentException("Value [" + name + "] not found");
		}
		StateValueObserver observer = new StateValueObserver(value,getDataSeries(dataSeries),incremental);

		scheduledTasks.add(new ExperimentTask(scheduler, observer,delay,frequency,0));
	}


	public void addStateObserver(String name,String dataSeries,long frequency,long delay){
	  addStateObserver(name, dataSeries, frequency, delay,false);
	}
	/**
	 * Starts the experiment. Executes the initialization tasks and then the first run.
	 * 
	 * @throws ExperimentException
	 */
	public void start() throws ExperimentException{

		try {
			Thread.currentThread().sleep(delay*1000);
		} catch (InterruptedException e) {
			throw new ExperimentException("Interrupted during start delay",e);
		}
		

		//execute initialization tasks
		for(Runnable r: initializationTasks){
			r.run();
		}

		//load tables
		for(Table t: tables.values()){
			try {
				t.load();
			} catch (TableException e) {
				throw new ExperimentException("Exception loading table "+t.getName(),e);
			}
		}

		for(Entry<String, Stream> s: streams.entrySet()){
			try {
				s.getValue().open();
			} catch (StreamException e) {
				throw new ExperimentException("Exception initialization stream "+s.getKey(),e);
			}
		}

		//execute first run
		executeRuns();

	}


	/**
	 * Start each of the runs of the experiment
	 * @throws ExperimentException
	 */
	private void executeRuns() throws ExperimentException{

		do{
			//schedule the end of run task
			//If the experiment has an end time, schedule its termination
			if(runLength != 0){

				Runnable endTask = new Runnable(){
					public void run(){
						endRun();
					}
				};

				//scheduledTasks.add(new ExperimentTask(scheduler,endTask,runLength,0));
				scheduler.setTerminationHandler(endTask);
			}

			runDir = new File(rootDir.getAbsolutePath(),String.valueOf(runs));
			runDir.mkdir();
			workingDir = runDir;
			

			//execute initiation tasks
			for(Runnable r: beginRunTasks){
				r.run();
			}

			//schedule the experiment's tasks (e.g. observers)
			for(ExperimentTask t: scheduledTasks){
				t.schedule();
			}

			//initiate scheduling of tasks
			scheduler.start(runLength);	

			executionLock.lock();
			try{
				endRunCondition.await();

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				executionLock.unlock();
			}


			//prevent the scheduler to further dispatch actions
			scheduler.pause();

			this.runTime = scheduler.getTime();
			this.endTime = System.currentTimeMillis();

			//execute termination tasks
			for(Runnable r: endRunTasks){
				r.run();
			}
			
			runs--;
			if(runs > 0){
				//prepare for a new run, clean up simulation
				reset();
			}

		}while(runs > 0);
		
		workingDir = rootDir;

		//execute tasks at the end of the experiment
		for(Runnable r: terminationTasks){
			r.run();
		}

		if(exitOnEnd){
			System.exit(0);
		}

	}


	/**
	 * Prepares for a new run
	 */
	private void reset(){

		//remove any pending task
		scheduler.pause();
		scheduler.reset();

		//reset values
		for(Counter c: counters.values()){
			c.reset();
		}

		//reset non persistent DataSeries 
		for(DataSeries d: series.values()){
			if(!persistentDS.contains(d)){
				d.reset();
			}
		}

		//reset Streams
		for(Stream s: streams.values()){
			s.reset();
		}

		//reset models 
		for(Model m: models.values()){
			m.reset();
		}

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
	 * Ends the execution of the run. 
	 * This method is executed under the scheduler's thread.
	 */
	public void endRun(){	
		
		executionLock.lock();
		try{
			endRunCondition.signal();
		}
		finally{
			executionLock.unlock();
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
	 * Adds a task whose run() method will be invoked before the experiment runs are started.
	 * Can be used to setup the environment, creating data files, etc.
	 * 
	 * There is no guarantee that tasks will be executed in the same order than they were added
	 * and actual implementation might run them in sequence or parallel.
	 * 
	 * @param task
	 */
	public void addInitilizationTask(Runnable task){
		initializationTasks.add(task);
	}

	/**
	 * Adds a task whose run() method will be invoked after all experiment runs 
	 * are executed and before the experiment ends.
	 * 
	 * There is no guarantee that tasks will be executed in the same order than they were added
	 * and actual implementation might run them in sequence or parallel.
	 *  
	 * @param task
	 */
	public void addFinalizationTask(Runnable task){
		terminationTasks.add(task);
	}

	/**
	 * Adds a task whose run() method will be invoked before each experiment run 
	 * is executed.
	 * 
	 * There is no guarantee that tasks will be executed in the same order than they were added
	 * and actual implementation might run them in sequence or parallel.
	 *  
	 * @param task
	 */

	public void addBeginRunTask(Runnable task){
		beginRunTasks.add(task);
	}


	/**
	 * Adds a task whose run() method will be invoked after each experiment run 
	 * is executed.
	 * 
	 * There is no guarantee that tasks will be executed in the same order than they were added
	 * and actual implementation might run them in sequence or parallel.
	 *  
	 * @param task
	 */

	public void addEndRunTask(Runnable task){
		endRunTasks.add(task);
	}


	/**
	 * Adds a task whose run() method will be invoked at the given frequency 
	 * 
	 * @param task
	 */
	public void addPeriodicTask(Runnable task,long delay, long frequency){

		scheduledTasks.add(new ExperimentTask(scheduler,task,delay,frequency,0));
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

		scheduledTasks.add(new ExperimentTask(scheduler,target, delay));

	}


	/**
	 * Adds a model to the experiment to be started at time 0
	 * @param model
	 */
	public void addModel(Model model){
		addModel(model,0);
	}


	public Model getModel(String name){
		Model model = models.get(name);
		if(model == null){
			throw new IllegalArgumentException("Model "+ name +" not registered in the experiment");
		}

		return model;
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
		observers.add(new FilteringEventObserver(new EventTypeFilter(events),observer));
	}

	/**
	 * Add an observer with a filter
	 * 
	 * @param observer
	 * @param filter
	 */
	public void registerEventObserver(EventObserver observer,EventFilter filter){
		observers.add(new FilteringEventObserver(filter,observer));	
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


	/**
	 * Takes a snapshot of the state of the Experiment
	 * 
	 * @return a map with the values
	 */
	public Map<String,Double> getState() {

		Map<String,Double> values = new HashMap<String,Double>();
		for(StateValue v: stateValues.values()){
			values.put(v.getName(), v.getValue());
		}

		return values;
	}


	/**
	 * 
	 * @return a Map with the Streams defined in the experiment
	 */
	public Map<String,Stream> getStreams() {
		return new HashMap(streams);
	}


	/**
	 * @return a Map with the Tables defined in the experiment
	 */
	public Map<String,Table> getTables(){
		return new HashMap(tables);
	}



	@Override
	public Configuration getConfiguration() {
		throw new UnsupportedOperationException();
	}



	@Override
	public ExecutionService getExecutionService() {
		return this;
	}



	@Override
	public void doWait(long time) {
		// TODO Auto-generated method stub

	}



	@Override
	public Task execute(Runnable task) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public long getCurrentTime() {
		return scheduler.getTime();
	}



	@Override
	public Task scheduleRepetitiveTask(Runnable task, long delay, long period) {
		throw new UnsupportedOperationException();	
	}



	@Override
	public Task scheduleTask(Runnable task, long delay) {
		throw new UnsupportedOperationException();
	}



}
