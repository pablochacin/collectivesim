package edu.upc.cnds.collectivesim.model.base;

import java.util.ArrayList;
import java.util.List;

import edu.upc.cnds.collectivesim.model.CompositeBehavior;
import edu.upc.cnds.collectivesim.model.Model;
import edu.upc.cnds.collectivesim.model.CompositeBehavior.SimpleBehavior;
import edu.upc.cnds.collectivesim.stream.Stream;
import edu.upc.cnds.collectivesim.stream.base.FixedValueStream;

public class CompositeBehaviorVisitor extends ModelAction {

    private static final String MODEL_ACTION_TYPE = "Behavior";
	
	private List<BehaviorVisitor> visitors;
	
	private Model model;
	
	private String name;
	
	public CompositeBehaviorVisitor(Model model,String name,CompositeBehavior behaviors,boolean active, int iterations,
			Stream<Long> frequency, long delay, long endTime, int priority) {
		
		super(model,name,active,iterations,frequency,delay,endTime,priority);
		
		visitors = new ArrayList<BehaviorVisitor>(behaviors.getBehaviors().size());
		for(SimpleBehavior b: behaviors.getBehaviors()) {
			visitors.add(new BehaviorVisitor(model,name+"."+b.method,b.sampler,b.method,true,
                    (int)0,new FixedValueStream<Long>("", (long)0),(long)0,(long)0,(int)0,b.args));
		}		
	  }

	@Override
	protected void execute() {
		
		for(BehaviorVisitor b: visitors) {
			b.execute();
		}
	}

	@Override
	protected String getType() {
		return MODEL_ACTION_TYPE;
	}

}
