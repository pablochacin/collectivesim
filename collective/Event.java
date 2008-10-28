package edu.upc.cnds.collectivesim.collective;

import edu.upc.cnds.collectivesim.models.Model;
import edu.upc.cnds.collectivesim.models.imp.Action;
import edu.upc.cnds.collectivesim.models.imp.BasicModel;


/**
 * 
 * @author pchacin
 *
 */
public class Event {

    /**
     *  Scope of agent: occurts to all agents or to one choosen randomly
     */
    public static long EVENT_SCOPE_ALL = 0;
    public static long EVEN_SCOPE_RANDOM = 1;


    /**
     * Model on which the behavior inhabits
     */
    Model model;

    /**
     * Realm on which the agents this model applies to, reside
     */
    Collective realm;
    /**
     * Stream that defines the inter event time
     */
    private String interEventTime;
    /**
     * Name of the method to be executed
     */
    private String method;

    /**
     * name of the Event
     */
    private String name;

    /**
     * scope of the event
     */
    private long scope;
    
    /**
     * Indicates if the event is active
     */
    private boolean active=false;
    
    /**
     * Number of events to generate
     */
    private long numEvents;
    
    /**
     * Default constructor
     * @param name a String that identifies this event
     * @param model the simulation Model on which this event occurs
     * @param realm the AgentRealm on which resides the agents this event will be applied to
     * @param methods a String array with the name of the methods to be execute
     * @param frequency a long with the frequency, in ticks, of execution
     * @param scope indicates if event occurs to all agent or to a sub-set of them
     * @param numEvents a long with the number of events to generate, 0 means  unlimited. 
     *        After this number is reached, the event is paused
     */
    protected Event(String name, Model model,Collective realm,String method, String interEventTime, boolean active, long scope, long numEvents){
        this.name = name;
        this.method = method;
        this.interEventTime = interEventTime;
        this.model = model;
        this.realm = realm;
        this.scope = scope;
        this.numEvents = numEvents;
        
        //if behavior must be created active
        if(active){
            start();
        }
    }

    /**
     * starts the execution of the behavior 
     */	
    public void start(){
        if(!active){
            active = true;
            schedule();
        }
    }
    
    
    /**
     * Schedule the execution of the event
     */
    private void schedule(){
        //get the inter event time from the random stream
        Double nextTime =  model.getTime() + model.getStreamValue(interEventTime);
               
        //schedule the execution of the behavior at the given frequency
        Action execute = new Action(this,"step",nextTime,false);
        model.scheduleAction(execute);	
    }

    /**
     * pause the execution of the behavior
     */
    public void pause(){
        if(active){
            active = false;
            //TODO:remove scheduled action
        }
    }



    /**
     * Schedulled method. Execute behavior's actions. 
     */
    public void step(){
        
        preProcessing();
        execute();
        postProcessing();
        
        //decrement the event counter
        numEvents--;
        
        //schedule next execution ifevent is still active and there are still pending events
        // (if numEvents was set to 0 when the event was created, this conditions always is true)
        if(active && (numEvents != 0)){
            schedule();
        }
    }
    
    
    /**
     * executes the method according with the scope
     *
     */
    public void execute(){

        //decide how to execute event
        if(scope == EVENT_SCOPE_ALL){
            realm.visit(method);
        }
        else{
            realm.visitRandom(method);
        }

    }
    
    
    /**
     * Prepare for this step.
     *
     */
    public void preProcessing(){
        //This method can be extended by subclasses
    }

    /**
     * Clean up after step.
     */
    public void postProcessing(){
        //This method can be extended by subclasses
    }


}
