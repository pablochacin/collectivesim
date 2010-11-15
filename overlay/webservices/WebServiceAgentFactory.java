package edu.upc.cnds.collectivesim.overlay.webservices;

import edu.upc.cnds.collectives.adaptation.AdaptationFunction;
import edu.upc.cnds.collectives.adaptation.bounded.BoundedRationalityAdaptation;
import edu.upc.cnds.collectives.adaptation.probabilistic.ProbabilisticAdaptation;
import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.overlay.Overlay;
import edu.upc.cnds.collectives.underlay.Underlay;
import edu.upc.cnds.collectivesim.CollectiveSim;
import edu.upc.cnds.collectivesim.model.Model;
import edu.upc.cnds.collectivesim.overlay.OverlayAgent;
import edu.upc.cnds.collectivesim.overlay.OverlayAgentFactory;
import edu.upc.cnds.collectivesim.overlay.OverlayFactory;
import edu.upc.cnds.collectivesim.overlay.OverlayModel;
import edu.upc.cnds.collectivesim.overlay.utility.ResponseTimeUtilityFunction;
import edu.upc.cnds.collectivesim.overlay.utility.UtilityFunction;
import edu.upc.cnds.collectivesim.stream.Stream;
import edu.upc.cnds.collectivesim.stream.base.EmpiricalRandomStream;
import edu.upc.cnds.collectivesim.stream.base.RandomWalkStream;

public class WebServiceAgentFactory extends OverlayAgentFactory {

	
	protected Integer requestLimit;
	
	protected Double serviceRate;
	
	protected Double targetServiceTime;
	
	protected Double alpha;
	
	protected Double minLoad,maxLoad;
	
	protected Double trend;
	
	protected Double drift;
	
	protected Double variation;
	
	public WebServiceAgentFactory(OverlayFactory factory, Underlay underlay,
			Stream<Identifier> ids,Integer requestLimit,Double serviceRate,
			Double targetServiceTime,Double alpha,Double minLoad,Double maxLoad,Double variation,Double trend,Double drift) {
		
		super(factory, ids);
		this.requestLimit = requestLimit;
		this.serviceRate = serviceRate;
		this.targetServiceTime = targetServiceTime;
		this.alpha = alpha;
		this.minLoad = minLoad;
		this.maxLoad = maxLoad;
		this.variation = variation;
		this.drift = drift;
		this.trend = trend;
		}

	
	@Override
	protected OverlayAgent createOverlayAgent(OverlayModel model, Overlay overlay) {		
		
	    String[] attributes = {"Id","Capacity","Utility","ResponseTime",
				"AcceptanceRate","BackgroudLoad","Utilization"};

		
		//	return new DiscreteTimeWebServiceAgent(model,overlay,overlay.getLocalNode().getId(),
		//			                   getUtilityFunction(),getTarget(),getAdaptationFunction(),requestLimit,getLoadStream(),serviceRate);
			
			
			return new DiscreteEventWebServiceAgent(model,overlay,
	                   getUtilityFunction(),getTarget(),getAdaptationFunction(),requestLimit,getLoadStream(),1000,100.0);

	}


	private UtilityFunction getUtilityFunction() {
	
		return new ResponseTimeUtilityFunction(targetServiceTime,alpha);
	}
	
	
	private Stream<Double> getLoadStream(){
		//return new RandomWalkStream("", minLoad, maxLoad, variation, drift, trend);
		
		return new EmpiricalRandomStream("", minLoad, maxLoad, 
				 CollectiveSim.getExperiment().getTable("distribution.skewed.1"));
	}
	
	
	private AdaptationFunction getAdaptationFunction(){
		return new ProbabilisticAdaptation(0.0,1.0,0.5,0.05,0.25);
		//return new BoundedRationalityAdaptation(0.0,1.0,0.1,0.05);
	}
	
	private Double getTarget(){
		return 0.7;
	}
}
