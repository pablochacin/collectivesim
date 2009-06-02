package edu.upc.cnds.collectivesim.model.base;

import java.util.Map;

import edu.upc.cnds.collectivesim.dataseries.DataSeries;
import edu.upc.cnds.collectivesim.dataseries.SeriesFunction;
import edu.upc.cnds.collectivesim.dataseries.base.BaseDataItem;
import edu.upc.cnds.collectivesim.model.AgentSampler;
import edu.upc.cnds.collectivesim.model.Model;
import edu.upc.cnds.collectivesim.stream.Stream;

public class CalculatingObserverVisitor extends ObserverVisitor {

	protected SeriesFunction function;
	
	protected int itemCount;
	
	public CalculatingObserverVisitor(Model model, String name,
			AgentSampler sampler, String[] attributes, DataSeries values,SeriesFunction function,
			boolean reset, int iterations, Stream<Long> frequency, long delay,
			long endTime) {
		
		
		super(model, name, sampler, attributes, values, reset, iterations, frequency,
				delay, endTime);
		
		this.function = function;
		this.itemCount = 0;
	}

	
	@Override
	protected void startVisit(){
	
		super.startVisit();
		
		function.reset();
		
		itemCount  =0;
		
	}
	
	/**
	 * 
	 * @param attributes
	 */
	protected boolean processAttributes(Map agentAttributes){

		return function.processItem(new BaseDataItem(++itemCount, agentAttributes));

	}


	@Override
	protected void endVisit() {
		function.calculate(values);

		super.endVisit();
	}
	
	
}
