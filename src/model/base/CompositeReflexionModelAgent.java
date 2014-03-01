package collectivesim.model.base;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import collectives.util.FormattingUtils;
import collectives.util.ReflectionUtils;
import collectivesim.model.ModelException;

/**
 * Implements a Reflection based Agent Model by composing a list of target objects. 
 * 
 * Methods are invoked on this list of targets, in the order given in the constructor, 
 * until one is matched or an exception arises.
 * 
 * The resulting agent behaves as if it had inherited methods from multiple super-classes.
 * Subclasses can override methods of the composite objects.
 * 
 * On the contrary to ReflexionModelAgent, which can take the name from the target's class name,
 * As this agent is created from multiple targets, the name must be explicitly given
 * or the name of this class (or any sub-class) is used.
 *
 * @author Pablo Chacin
 *
 */
public class CompositeReflexionModelAgent extends ReflexionModelAgent {

	protected List<Object> targets;


	public CompositeReflexionModelAgent(String name,String[] attributes,Object ...targets){
		super(name,attributes);
		this.targets = new ArrayList<Object>();
		this.targets.add(this);
		setTargets(targets);
	}

	
	protected void setTargets(Object...targets){
		this.targets.addAll(Arrays.asList(targets));
	}

	@Override
	public void execute(String action, Object ...args) throws ModelException {

		
		Class[] classes = ReflectionUtils.getClasses(args);

		try{
			for(Object target: targets){
				try{
					ReflectionUtils.invoke(target, action, classes,args);
					return;
				}
				catch(NoSuchMethodException e){
					continue;
				}
			}
			
			throw new NoSuchMethodException(action);
		}

		catch (Exception e) {
			throw new ModelException("Exception executing action "+ action +" " +FormattingUtils.getStackTrace(e));
		}

	}


	@Override
	public void execute(String action) throws ModelException {
		execute(action, new Object[0]);
	}


	
	@Override
	public Object inquire(String attribute) throws ModelException {

		String accessor = "get"+attribute;

		try{
			for(Object target: targets){
				try{
					return ReflectionUtils.invoke(target, accessor,new Class[0],new Object[0]);
				}
				catch(NoSuchMethodException e){
					continue;
				}
			}
			
			throw new NoSuchFieldException(attribute);
			
		}catch (Exception e) {
			throw new ModelException("Exception accesssing attribute "+ attribute +" "+FormattingUtils.getStackTrace(e));
		}
		

	}


	
}
