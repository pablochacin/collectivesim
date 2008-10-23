/*
 * Model.java
 * 
 * 
 * (c) Pablo Chacin 2006
 */
package edu.upc.cnds.collectivesim.models;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.engine.SimInit;
import uchicago.src.sim.engine.SimModelImpl;
import uchicago.src.sim.gui.DisplaySurface;
import cern.jet.random.*;
import cern.jet.random.engine.MersenneTwister;
import cern.jet.random.engine.RandomEngine;
import edu.upc.cnds.collectivesim.agents.Agent;
import edu.upc.cnds.collectivesim.metrics.Metric;
import edu.upc.cnds.collectivesim.metrics.MetricsReporter;
import edu.upc.cnds.collectivesim.views.View;

/**
 * 
 * A Model represents the root of a simulation hierarchi. 
 * Is the anchor point for Realms and Views.
 * Offers the interface to control del simulation.
 * Must be extended to fit an specific simulation model. 
 * 
 * @author Pablo Chacin <br>
 */
public abstract class BasicModel extends SimModelImpl implements Model {
  
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
  private ArrayList realms;

  /**
   * Agents of this model
   */
  private ArrayList agents;
  
  /**
   * Random number generator
   */
  private Random rand = new Random();

  /**
   * Name of the model
   */
  private String name;
  
  /**
   * random streams defined in this model
   */
  HashMap streams = new HashMap();
  
  /**
   * Default metrics reporter
   */
  
  /**
   * Reporter used to report metrics
   */
  private MetricsReporter reporter;
  
  /**
   * Path to report metrics
   */
  private String metricsPath = ".";
 
  /**
   * List of metrics to collect
   */    
   private String metricsList = "";

   /**
    * Default random engine used to create random distributions
    */
private RandomEngine randomGenerator = new MersenneTwister();   
      
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
    agents = new ArrayList();
    realms = new ArrayList();
    
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
   * Populate the model with realms and agents
   */
  public abstract void buildModel();
  
 
  /**
   * Add a view to the model
   */
  public void addView(View view){
	  //Register view
	  views.add(view);
     
  }

  /**
   * Add a new agent to this model's agent list 
   * TODO: Check if it is necessary to register agents.
   */
  private void addAgent(Agent a){
    agents.add(a);
   
  }
  
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
  

  /* (non-Javadoc)
 * @see simrealms.models.ModelInterface#getNumActiveAgents()
 */
  public int getNumActiveAgents(){
    return agents.size();
  }

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
  public void scheduleAction(Action action){
      if(action.isRepetitive()){
          schedule.scheduleActionAtInterval(action.getFrequency(),action.getTarget(),action.getMethod());
      }
      else{
          schedule.scheduleActionAt(action.getFrequency(),action.getTarget(),action.getMethod());
      }
  }
  
  
  /* (non-Javadoc)
 * @see simrealms.models.Model#getRandomDouble()
 */
  public Double getRandomDouble(){
      return rand.nextDouble();
  }

/* (non-Javadoc)
 * @see simrealms.models.ModelInterface#getBooleanProperty(java.lang.String)
 */
public Boolean getBooleanProperty(String property) {

    return (Boolean)getObjectProperty(property);
}

/* (non-Javadoc)
 * @see simrealms.models.ModelInterface#getDoubleProperty(java.lang.String)
 */
public Double getDoubleProperty(String property) {
       return (Double)getObjectProperty(property);
}

public String getStringProperty(String property) {
    return (String)getObjectProperty(property);
}

/* (non-Javadoc)
 * @see simrealms.models.ModelInterface#getObjectProperty(java.lang.String)
 */
public Object getObjectProperty(String property) {
    Method method;
    Object result = null;
    try {
        method = this.getClass().getMethod("get"+property, null);
        result = method.invoke(this, null);
    } catch (SecurityException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (NoSuchMethodException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (IllegalArgumentException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (IllegalAccessException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (InvocationTargetException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    
    return result;
}


/* (non-Javadoc)
 * @see simrealms.models.ModelInterface#reportMetric(simrealms.metrics.Metric)
 */
public void reportMetric(Metric metric){
    reporter.writeMetric(metric);
}




/* (non-Javadoc)
 * @see simrealms.models.ModelInterface#reportMetric(java.lang.String, java.lang.String, java.util.Map)
 */
public void reportMetric(String name,String value,Map attributes){
    Metric metric = new Metric(name,value,schedule.getCurrentTime(),attributes);
}

/* (non-Javadoc)
 * @see simrealms.models.ModelInterface#reportMetric(java.lang.String, java.lang.String)
 */
public final void reportMetric(String name, String value){
    
    //report the metrics 
    reportMetric(name,value, new HashMap());
}



/**
 * Defines the metrics reporter
 */

public void setReporter(MetricsReporter reporter){
    this.reporter = reporter;
}

/**
 * Opens the metrics reporter, 
 * 
 * @param metrics a String with the list of metrics to collect 
 * @param metricsPath path where the metrics must be collected
 *
 */
public void openMetricsReporter(String metrics, String metricsPath){
    this.reporter.open();
}

/**
 * get the current path for metrics 
 */
public String getMetricsPath(){
    return this.metricsPath;
}

public void setMetricsPath(String path){
    this.metricsPath = path;
}

/**
 * Returns the list of metrics been collected
 * 
 * @return a strng with the name of the metrics, separated by commas
 */
public String getMetricsList(){
    return this.metricsList;
}

/**
 * Sets the list of metrics to collect
 * 
 * @param list a String with the name of the metrics,separated by commas
 */
public void setMetricsList(String list){
    this.metricsList = list;
}


/* (non-Javadoc)
 * @see simrealms.models.ModelInterface#getTime()
 */
public Double getTime(){
    return new Double(schedule.getCurrentTime());
}


public Double getStreamValue(String stream){
    
    AbstractDistribution distribution = (AbstractDistribution)streams.get(stream);
    if(distribution == null){
        throw new IllegalStateException("Stream not initialized");
    }
    
    return distribution.nextDouble();
}

/**
 * register a random stream 
 * @param name a String with the name of the stream
 * @param distribution an AbstractDistribution with the stream's random distribution
 */
public void addStream(String name,AbstractDistribution distribution){
    streams.put(name,distribution);
}


/**
 * Returns a default random generator used as parameter to create random distributions
 * 
 * @return a RandomEngine 
 */
public RandomEngine getRandomGenerator(){
    return this.randomGenerator;
}

}



