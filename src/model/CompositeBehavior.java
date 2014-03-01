package collectivesim.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import collectivesim.stream.Stream;

/**
 * Represents a composite behavior, each one 
 * 
 * @author Pablo Chacin
 *
 */
public class CompositeBehavior {
	
	public static class SimpleBehavior{
		public String method;
		
		public AgentSampler sampler;
		
		public Stream[] args;

		public SimpleBehavior(AgentSampler sampler,String method, Stream[] args) {
			super();
			this.method = method;
			this.sampler = sampler;
			this.args = args;
		}
		
		
	}
	
	private List<SimpleBehavior> behaviors;
	
	
	public CompositeBehavior() {
		this.behaviors = new ArrayList<SimpleBehavior>();
	}
	
	
	public void addBehavior(AgentSampler sampler,String method,Stream...args) {
		
		behaviors.add(new SimpleBehavior(sampler,method,args));
	}
	
	public List<SimpleBehavior> getBehaviors(){
		return behaviors;
	}
}
