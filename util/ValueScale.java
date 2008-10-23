package edu.upc.cnds.collectivesim.util;

import java.awt.Color;

/**
 * 
 * Allowa the mapping of values to a scale
 * 
 * @author Pablo Chacin
 *
 */
public class ValueScale {
	
	private Double min;
	private Double max;
	private Double levels;
	private boolean log;
	private boolean open;
	private Double levelRange;
	/**
	 * sets the color scale used to map attribute values to 
	 * colors. Scales are a series of steps. that defines the levels of values
	 * (level 0, level 1, ... level n)
	 * 
	 * In closed scales the values above maximun or below minimun are ingnored.
	 * 
	 * In open scales, any value below the minimun value is considered par of the level 0.
	 * Any value above the maximun value is considered to be in the level n-1
	 * limits of levels 1, 2... n-2 are defined splittin the range [min,max] in 
	 * n-2 equal levels.
	 * 
	 *  
	 * If the scale is lineal, the size of each level is (max-min)/(levels-2) 
	 *  
	 * @param min minimun value in the scale
	 * @param max maximun value in the scale
	 * @param levels number of levels in the scale
	 * @param log a boolean that indicates if the scale is logaritmic (true) or lineal (false)
	 * @param open a boolean that indicates if the scale is open or closed. 
	 */
		public  ValueScale(Double min, Double max,int levels, boolean open,boolean log){
			this.max = max;
			this.min = min;
			this.levels= new Double(levels);
			this.levelRange = (this.max-this.min)/this.levels;
			this.log = log;
			this.open = open;
		

		}
		
		/**
		 * Returns the number of levels
		 */
		public int getLevels(){
			return this.levels.intValue();
		}
		
		/**
		 * Returns the maximun value of the scale
		 */
		public Double getMax(){
			return this.max;
		}
		
		/**
		 * Returns the maximun value of the scale
		 */
		public Double getMin(){
			return this.min;
		}
		
		/**
		 * calculates on wich step is located the current value of the agent
		 * attribute
		 * 
		 * @return the step, counting from 0, on which the current value falls. 
		 *         if the 
		 */
		public int mapToScaleLevel(Double value){
			
			//is a logaritmic scale
			if (log){
				throw new UnsupportedOperationException();
			}
			
			if(open){
				throw new UnsupportedOperationException();
			}
			else{
				
				//check the value is valid
				if(!isInRange(value)){
					throw new IllegalArgumentException("Value out of range");
				}
				
			    return (int)(value / levelRange);
			}

			
		}

		/**
		 * Checks if the value is in the valid range
		 * @param value
		 * @return
		 */
		public boolean isInRange(Double value) {
			
			//for open scales, values are allways valid
			if(open){
				
				return true;
			}
            //if scale is closed (not open) value must fall within boundaries
			else{
				return((value >= min) || (value <= max));
			}
			
		}
}
