package edu.upc.cnds.collectivesim.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * Selects a random sample of ModelAgents from a list, up to a given
 * maximum. 
 * 
 * @author Pablo Chacin
 *
 */
public class RandomAgentSampler implements AgentSampler {

	private int maxSampleSize;

	public RandomAgentSampler(int size){
		this.maxSampleSize = size;
	} 
	
	
	@Override
	/**
	 * Generates a random sample by shuffling the list and selecting the heading
	 * sublist up to the maximum between the max sample size and the actual 
	 * size of the list
	 */
	public List<ModelAgent> sample(List<ModelAgent> agents) {
		
		int sampleSize = Math.min(maxSampleSize, agents.size());
		
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
