package edu.upc.cnds.collectivesim.table.base;

import java.io.BufferedReader;
import java.io.FileReader;

import edu.upc.cnds.collectivesim.experiment.ExperimentException;
import edu.upc.cnds.collectivesim.table.TableException;

/**
 * Loads a table from a file. 
 * 
 * Allows more than one value per line, separated by a delimiter. Default delimiter is ","
 *
 * Allows comment lines begenning with "#"
 * 
 * @author Pablo Chacin
 *
 * @param <T>
 */
public class TextFileTable<T> extends MemoryTable<T> {

	private BufferedReader reader;
	
	private String file;
	
	private Class type;
	
	public TextFileTable(String name,String file,String delimiter,Class type) {
		super(name);
		this.file = file;
		this.type = type;
	}

	@Override
	public void load() throws TableException{
		
		try {
			reader = new BufferedReader(new FileReader(file));
			String values = reader.readLine();
			while(values != null){
				
				//skip comment lines
				if(values.startsWith("#")){
					continue;
				}
				
				parseValues(values,"",type);
				values = reader.readLine();
			}
		} catch (Exception e) {
			throw new TableException("Exception loading table "+name + " from file "+file,e);
		}

		
	}
}
