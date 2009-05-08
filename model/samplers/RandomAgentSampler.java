package edu.upc.cnds.collectivesim.model.samplers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.upc.cnds.collectivesim.model.AgentSampler;
import edu.upc.cnds.collectivesim.model.ModelAgent;
/**
 * Selects a random sample of ModelAgents from a list, up to a given
 * maximum. This maximum can be specified either as a absolute number or
 * as a fraction of the number of agents.
 * 
 * @author Pablo Chacin
 *
 */
public class RandomAgentSampler implements AgentSampler {

	/**
	 * Maximimun size of the sample
	 */
	private int maxSampleSize;
	
	/**
	 * Maximun Fraction of the agents to sample
	 */
	private double fraction;

	public RandomAgentSampler(int size){
		this.maxSampleSize = size;
		this.fraction = 1.0;
	} 
	
	public RandomAgentSampler(double fraction){
		this.maxSampleSize = 0;
		this.fraction = fraction;
	}
	
	
	@Override
	/**
	 * Generates a random sample by shuffling the list and selecting the heading
	 * sublist up to the maximum between the max sample size and the actual 
	 * size of the list
	 */
	public List<ModelAgent> sample(List<ModelAgent> agents) {
		
		int sampleSize;
		if(maxSampleSize != 0){
			sampleSize = maxSampleSize;
		}
		else{
			sampleSize = (int) Math.round((double)agents.size()*fraction);
		}
		
		sampleSize = Math.min(sampleSize, agents.size());
		
		List<ModelAgent> sample = new ArrayList<ModelAgent>(sampleSize);
		
		if(sampleSize == 0){
			return sample;
		}
		
		//TODO: if maxSampleSize == agents.size(), is not necessary to copy
		//everything around
		List<ModelAgent> randomList = new ArrayList<ModelAgent>(agents);
		Collections.shuffle(randomList);
		sample.addAll(randomList.subList(0, sampleSize));
		
		return sample;
	}

}
