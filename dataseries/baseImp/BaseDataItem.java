package edu.upc.cnds.collectivesim.dataseries.baseImp;

import java.util.HashMap;
import java.util.Map;

import edu.upc.cnds.collectivesim.dataseries.DataItem;

public class BaseDataItem implements DataItem {

	private Double value;
	
	private Map categories;
		
	public BaseDataItem(Double value, Map categories) {
		super();
		this.value = value;
		this.categories = categories;
	}

	public BaseDataItem(Double value) {
		this(value, new HashMap());
	}
	
	public Map getAttributes() {
		return categories;
	}

	public Double getValue() {
		return value;
	}

}
