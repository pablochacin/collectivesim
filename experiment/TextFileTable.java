package edu.upc.cnds.collectivesim.experiment;

import java.io.BufferedReader;
import java.io.FileReader;

import edu.upc.cnds.collectivesim.experiment.imp.MemoryTable;

public class TextFileTable<T> extends MemoryTable<T> {

	private BufferedReader reader;
	
	private String file;
	
	private Class type;
	
	public TextFileTable(String name,String file,Class type) {
		super(name);
		this.file = file;
		this.type = type;
	}

	@Override
	public void load() throws ExperimentException{
		
		try {
			reader = new BufferedReader(new FileReader(file));
			String value = reader.readLine();
			while(value != null){
				addValue(value,type);
				value = reader.readLine();
			}
		} catch (Exception e) {
			throw new ExperimentException("Exception loading table "+name,e);
		}

		
	}
}
