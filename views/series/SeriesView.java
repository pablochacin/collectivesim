package edu.upc.cnds.collectivesim.views.series;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import edu.upc.cnds.collectivesim.collective.Collective;
import edu.upc.cnds.collectivesim.models.Model;
import edu.upc.cnds.collectivesim.models.imp.Action;
import edu.upc.cnds.collectivesim.models.imp.BasicModel;
import edu.upc.cnds.collectivesim.views.View;

import uchicago.src.sim.analysis.OpenHistogram;
import uchicago.src.sim.analysis.OpenSequenceGraph;

public class SeriesView implements View {
    
    /**
     * 
     */
     private String title;
     
    /**
     * Repast's histogram object
     */
    private OpenSequenceGraph graph;
    
    /**
     * Model on which this view inhabits
     */
    private Model model;
    
    /**
     * Realm 
     */
    private Collective realm;
    
    
    /**
     * A HashMap with the values of each series 
     */
    private HashMap values;

    private Vector series;
    
    public SeriesView(BasicModel model, Collective realm,String title, long frequency){
        this.model = model;
        this.realm = realm;
        
        //create histogram with two additioanal bins to make room at both sides
        this.graph = new OpenSequenceGraph(title, model);;
  
        this.values= new HashMap();
        this.series = new Vector();
        this.title = title;
        
        //create an action to schedule the refreshment
        Action  refresh = new Action(this,"refresh",frequency,true);
        model.scheduleAction(refresh);
    }
    
    public void display() {
        this.graph.display();

    }

    public void dispose() {
        this.graph.dispose();
    }

    public String getTitle() {
        return this.title;
    }
    
    public void refresh(){
        
        //Update all the data series
        for(int i = 0;i<series.size();i++){
            ArrayList valuesList = (ArrayList)values.get(series.get(i));
            valuesList.clear();
            valuesList.addAll(realm.handelInquire((String)series.get(i)));
        }
        
        //update the graph
        graph.step();
    }
    
    /**
     * Adds a new data series to the graph.
     * @param attribute a String with the name of the agent's attribute from which the dataseries values
     *        will be taken.
     */
    public void AddSeries(String attribute){
        this.series.add(attribute);
        this.values.put(attribute, new ArrayList());
    }

    public void addMinSequence(String name, String series){
        ArrayList values = (ArrayList)this.values.get(series);
        graph.addSequence(name, new MinSequence(values));
    }


    public void addMaxSequence(String name, String series){
        ArrayList values = (ArrayList)this.values.get(series);
        graph.addSequence(name, new MaxSequence(values));
    }
    
    public void addAverageSequence(String name, String series){
        ArrayList values = (ArrayList)this.values.get(series);
        graph.addSequence(name, new AverageSequence(values));
    }
}
