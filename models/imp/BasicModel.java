/*
 * Model.java
 * 
 * 
 * (c) Pablo Chacin 2006
 */
package edu.upc.cnds.collectivesim.models.imp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.engine.SimInit;
import uchicago.src.sim.engine.SimModelImpl;
import edu.upc.cnds.collectives.events.EventReporter;
import edu.upc.cnds.collectivesim.models.ScheduledAction;
import edu.upc.cnds.collectivesim.models.SimulationModel;
import edu.upc.cnds.collectivesim.models.Stream;
import edu.upc.cnds.collectivesim.views.View;

/**
 * 
 * A Model represents the root of a simulation hierarchy. 
 * Offers the interface to control del simulation.
 * Must be extended to fit an specific simulation model. 
 * 
 * @author Pablo Chacin <br>
 */
public abstract class BasicModel extends SimModelImpl implements SimulationModel {
  
 /**
  * Repast's model Simulation schedule, used to schedule actions.
  * must be defined here due to restrictions in Repast's architecture.
  */
  private Schedule schedule;

  /**
   * Active views of this model
   */
  private  ArrayList views;

  /**
   * Realms of this model
   */
  private ArrayList collectives;


  /**
   * Name of the model
   */
  private String name;
  
  /**
   * random streams defined in this model
   */
  Map<String,Stream> streams = new HashMap();
  
  /**
   * Reporter used to report metrics
   */
  private EventReporter reporter;
  
 
      
  /**
   * Get a String that serves as the name of the model
   * @return the name of the model
   */
  public String getName(){
    return this.name;
  }

  /**
   * Sets the model's name
   */
  public void setName(String name){
	  this.name = name;  
  }
  
  /**
   * Tear down any existing pieces of the model and
   * prepare for a new run.
   */
  public void setup(){

    /*
     * dispose views
     */
	if(views != null){
		for(int i=0;i<views.size();i++){
			((View)views.get(i)).dispose();
		}
	}
	
	/*
	 * Create list of components (agents, realms, views)
	 */
	views  = new ArrayList();  
    collectives = new ArrayList();
    streams = new HashMap<String,Stream>();
    
    /*
     * create Repast's schedule
     */
    schedule = new Schedule(1);


  }

  /**
   * Initialize the model by building the separate elements that make
   * up the model
   */
  public void begin(){
    buildModel();

    /*
     * display each view 
     */
	for(int i=0;i<views.size();i++){
		((View)views.get(i)).display();
	}
  }

  /**
   * Populate the model with collectives and agents.
   */
  public abstract void buildModel();
  
   
  /**
   * Returns the Schedule object for this model; for use
   * internally by RePast
   * @return the Schedule object for this model
   */
  public Schedule getSchedule(){
    return schedule;
  }

  /**
   * Get the string array that lists the initialization parameters
   * for this model
   * @return a String array that includes the names of all variables
   * that can be modified by the RePast user interface
   */
  abstract public String[] getInitParam();
  


  /**
   * Initialize model method for this model object; this runs the model.
   * @param args Any string arguments to be passed to this model (currently none)
   */
  public void init() {
    SimInit init = new SimInit();
    init.loadModel(this, "", false);
  }

  /* (non-Javadoc)
 * @see simrealms.models.Schedule#scheduleAction(simrealms.models.Action)
 */
  public ScheduledAction scheduleAction(Runnable target, long delay) {
      
	  SingleAction action = new SingleAction(schedule,target,delay);

      schedule.scheduleActionAt(delay,action);
      
      return action;
  }
  
  
  public ScheduledAction scheduleRepetitiveAction(Runnable target,Stream distribution) {
	  
	  RepetitiveAction action = new RepetitiveAction(schedule,target,distribution);
	  
      schedule.scheduleActionAtInterval((Double)distribution.getValue(), action);
      
      return action;
  }

  



/**
 * Defines the metrics reporter
 */

public void setReporter(EventReporter reporter){
    this.reporter = reporter;
}



/* (non-Javadoc)
 * @see simrealms.models.ModelInterface#getTime()
 */
public Double getTime(){
    return new Double(schedule.getCurrentTime());
}


/**
 * register a random stream 
 * @param name a String with the name of the stream
 * @param distribution an AbstractDistribution with the stream's random distribution
 */
public void addStream(String name,Stream distribution){
    streams.put(name,distribution);
}


/**
 * Adds a view, schedulling it refresh at the given frequency
 * @param view View to add to the model
 * @param frequency time between sucesive refreshs
 */
public void addView(View view,long frequency) {

	
	scheduleRepetitiveAction(new ViewRefresher(view), new SingleValueStream(view.getTitle(),frequency));
	
}

}



