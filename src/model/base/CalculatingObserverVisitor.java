package collectivesim.model.base;

import java.util.Map;

import collectivesim.dataseries.DataSeries;
import collectivesim.dataseries.SeriesFunction;
import collectivesim.dataseries.base.BaseDataItem;
import collectivesim.model.AgentSampler;
import collectivesim.model.Model;
import collectivesim.stream.Stream;

public class CalculatingObserverVisitor extends ObserverVisitor {

	protected SeriesFunction function;
	
	protected int itemCount;
	
	public CalculatingObserverVisitor(Model model, String name,
			AgentSampler sampler, String[] attributes, DataSeries values,SeriesFunction function,
			boolean reset, int iterations, Stream<Long> frequency, long delay,
			long endTime,int priority) {
		
		
		super(model, name, sampler, attributes, values, reset, iterations, frequency, delay, endTime,priority);
		
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
