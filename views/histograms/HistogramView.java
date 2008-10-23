package edu.upc.cnds.collectivesim.views.histograms;

import java.util.ArrayList;

import edu.upc.cnds.collectivesim.collective.Collective;
import edu.upc.cnds.collectivesim.models.Action;
import edu.upc.cnds.collectivesim.models.BasicModel;
import edu.upc.cnds.collectivesim.models.Model;
import edu.upc.cnds.collectivesim.views.View;

import uchicago.src.sim.analysis.OpenHistogram;
import uchicago.src.sim.gui.Displayable;


public class HistogramView implements View {

	/**
	 * Repast's histogram object
	 */
	private OpenHistogram histogram;
	
	/**
	 * Model on which this view inhabits
	 */
	private Model model;
    
    /**
     * Realm 
     */
    private Collective realm;
    
    /**
     * Attribute to display in the histogram
     */
    private String attribute;
    
    /**
     * List of atttribute values 
     */
    private ArrayList values;
/**
 * Default constructor with all parameters
 * 
 * @param model a reference to the model this view inhabits
 * @param realm the AgentRealm on which the agents lives
 * @param title String with the title of the histogram
 * @param attribute a String with the name of the agent attribute to observe
 * @param numBins int with the number of bins (columns)
 * @param lowerBound int with the lower bound of the histogram's value
 * @param frequency of histogram refreshment
 */
	public HistogramView(BasicModel model, Collective realm,String title, String attribute,int numBins, long lowerBound, long frequency){
		this.model = model;
        this.realm = realm;
        //create histogram with two additioanal bins to make room at both sides
		this.histogram = new OpenHistogram(title, numBins+2, lowerBound);
        this.attribute = attribute;
		
        //get attribute values from realm
        values = realm.inquire(attribute);
        
        //bind the histogram with the agents in the realm
        HistogramDataVisitor valueVisitor = new HistogramDataVisitor();
        this.histogram.createHistogramItem(title, values, valueVisitor);
        
		//create an action to schedule the refreshment
		Action  refresh = new Action(this,"refresh",frequency,true);
		model.scheduleAction(refresh);
	}
	
	public void display() {
		this.histogram.display();

	}

	public void dispose() {
		histogram.dispose();

	}

	public Displayable getDisplayable() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTitle() {
    		// TODO Auto-generated method stub
    		return null;
	}

	/**
	 * Refresh the histograma
	 *
	 */
	@SuppressWarnings("unchecked")
    public void refresh(){
        values.clear();
        values.addAll(realm.inquire(this.attribute));
        histogram.step();
	}
}
