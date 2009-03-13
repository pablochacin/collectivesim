package edu.upc.cnds.collectivesim.identifier;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigInteger;

import edu.upc.cnds.collectives.identifier.BasicIdentifier;
import edu.upc.cnds.collectives.identifier.IdSpace;
import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectives.identifier.Imp.DirectedCircularIdSpace;

/**
 * Generates a sequence of ids to a file.
 * 
 * @author Pablo Chacin
 *
 */
public class IdFileGenerator {

	private String file;
	
	private PrintWriter output;
	
	int numIds;
	
	int length;
	
	private IdSpace space;
	
	public IdFileGenerator(String file, int numIds,int length){
		this.file = file;
		this.numIds =  numIds;
		this.length = length;
		this.space = new DirectedCircularIdSpace();
		
		try {
			this.output = new PrintWriter(new FileOutputStream(file));
		} catch (Exception e) {
			throw new IllegalArgumentException("File "+ file + "is not accessible",e);
		} 
		
	}
	
	public void generate() throws IOException {
	
		
		BigInteger distance = space.getMaxIdentifier().asBigInteger().divide(BigInteger.valueOf(numIds));
				
			
		BigInteger idValue = BigInteger.ZERO;
		for(long i = 0; i < numIds;i++){
			Identifier id = new BasicIdentifier(idValue,length);
			System.out.println(id.toString());
			output.println(id.toString());
			idValue = idValue.add(distance);
		}
		
		output.flush();
		output.close();
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		IdFileGenerator generator = new IdFileGenerator(args[0],
				Integer.valueOf(args[1]),
				Integer.valueOf(args[2]));
		
		try {
			generator.generate();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}