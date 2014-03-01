package collectivesim.model.samplers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import collectivesim.core.CollectiveSim;
import collectivesim.model.AgentSampler;
import collectivesim.model.ModelAgent;

/**
 * Makes a sample of (approximately) k agents following a Poisson process.
 * When this process is repeated, the average number of agents selected is
 * k, but on each invocation, it may vary (with a variace of sqrt(k).
 * 
 * If there are n agents, each agent will have a probability of k/n of being selected
 * in the sample.
 * 
 * @author Pablo Chacin
 *
 */
public class PoissonSampler implements AgentSampler {

	protected Integer n;

	protected Random rand;
	
	
	
	public PoissonSampler(Integer n){
		this.n = n;
		this.rand = CollectiveSim.getExperiment().getRandomGenerator();
	}
	
	
	
	@Override
	public List<ModelAgent> sample(List<ModelAgent> agents) {
		
		List<ModelAgent> sample = new ArrayList<ModelAgent>(n);
		
		for(int i=0;i<n;i++) {
			sample.add(agents.get(rand.nextInt(agents.size())));
		}
		
		return sample;
		
	}

}
