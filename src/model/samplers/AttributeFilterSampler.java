package collectivesim.model.samplers;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import collectives.util.FormattingUtils;
import collectivesim.model.AgentSampler;
import collectivesim.model.ModelAgent;
import collectivesim.model.ModelException;

/**
 * Filters a sample of agents based on the value of an attribute.
 * The attribute's value must implement the equals method.
 *  
 * @author Pablo Chacin
 *
 */
public class AttributeFilterSampler implements AgentSampler {

	protected static Logger log = Logger.getLogger("collectivesim.model");

	protected String attribute;
	
	protected Object value;
	
	public AttributeFilterSampler(String attribute,Object value){
		this.attribute = attribute;
		this.value = value;
	}
	
	@Override
	public List<ModelAgent> sample(List<ModelAgent> agents) {
		List<ModelAgent>selected = new ArrayList<ModelAgent>();
		for(ModelAgent a: agents){
			try {
				if(((String)a.inquire(attribute)).equals(value)){
					selected.add(a);
				}
			} catch (ModelException e) {
				//TODO throw an unchecked exception to stop observer
				log.warning("Exception accessing attribute " + attribute + 
						    FormattingUtils.getStackTrace(e));
			}
		}
		
		return selected;
	}

}
