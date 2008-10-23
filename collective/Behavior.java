package edu.upc.cnds.collectivesim.collective;

import edu.upc.cnds.collectivesim.models.Action;
import edu.upc.cnds.collectivesim.models.BasicModel;
import edu.upc.cnds.collectivesim.models.Model;


/**
 * 
 * @author pchacin
 *
 */
public class Behavior {

    /**
     * Order of execution of methods of the behavior
     */
    public static long BEHAVIOR_ORDER_BY_AGENT = 0;
    public static long BEHAVIOR_ORDER_BY_METHOD = 1;


    /**
     * Model on which the behavior inhabits
     */
    Model model;

    /**
     * Realm on which the agents this model applies to, reside
     */
    Collective realm;
    /**
     * Frequency of execution of the behavior
     */
    private double frequency;

    /**
     * Name of the methods to be executed
     */
    private String[] methods;

    /**
     * name of the mehavior
     */
    private String name;

    /**
     * order of execution of methods
     */
    private long order;
    
    
    /**
     * Indicates if the behavior is active
     */
    private boolean active=false;
    
    /**
     * Default constructor
     * @param name a String that identifies this behavior
     * @param model the simulation Model on which this behavior inhabits
     * @param realm the AgentRealm on which resides the agents this behavior will be applied to
     * @param methods a String array with the name of the methods to be execute
     * @param active a boolean that indicates if the behavior must be inserted active
     *        or will be deactivated until the realm activates it.
     * @param frequency a long with the frequency, in ticks, of execution
     * @param order order of execution (by agent or by method)
     */
    protected Behavior(String name, Model model,Collective realm,String[] methods, boolean active,
            double frequency, long order){
        this.name = name;
        this.methods = methods;
        this.frequency = frequency;
        this.model = model;
        this.realm = realm;
        this.order = order;

        //if behavior must be created active
        if(active){
            this.start();
        }
    }

    /**
     * starts the execution of the behavior 
     */	
    public void start(){
        //schedule the execution of the behavior with a repetitive action at 
        //the given frequency 
        if(!active){
          active = true;
          schedule();
        }
    }

    /**
     * pause the execution of the behavior
     */
    public void pause(){
        if(active){
          active = false;
          //TODO: pause the behavior
        }
    }

    /**
     * Schedule the execution of the behavior
     *
     */
    private void schedule(){
       Action execute = new Action(this,"step",frequency,true);
       model.scheduleAction(execute);
    }
    
    /**
     * Schedulled method. Execute behavior's actions. 
     */
    public void step(){
        preProcessing();
        execute();
        postProcessing();
    }
    
    
    /**
     * executes the methods on all agents
     *
     */
    public void execute(){

        //decide how to execute behavior: by agent or by method
        if(order == BEHAVIOR_ORDER_BY_AGENT){
            realm.visit(methods);
        }
        else{
            if(order == BEHAVIOR_ORDER_BY_METHOD){
                for(int i=0;i<methods.length;i++){
                    realm.visit(methods[i]);
                }
            }
            else{
                //TODO: handle this error!
            }
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
