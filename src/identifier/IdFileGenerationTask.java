package collectivesim.identifier;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;

import collectives.identifier.BasicIdentifier;
import collectives.identifier.IdSpace;
import collectives.identifier.Identifier;
import collectives.identifier.Imp.DirectedCircularIdSpace;

/**
 * Generates a sequence of ids to a file.
 * 
 * @author Pablo Chacin
 *
 */
public class IdFileGenerationTask implements Runnable{

	private String file;
	
	private PrintWriter output;
	
	int numIds;
	
	int length;
	
	private IdSpace space;
	
	public IdFileGenerationTask(String file, Integer numIds,Integer length){
		this.file = file;
		this.numIds =  numIds;
		this.length = length;
		this.space = new DirectedCircularIdSpace(length);
		
		try {
			this.output = new PrintWriter(new FileOutputStream(file));
		} catch (Exception e) {
			throw new IllegalArgumentException("File "+ file + "is not accessible",e);
		} 
		
	}
	
	public void run() {
	
		
		BigInteger distance = space.getMaxIdentifier().asBigInteger().divide(BigInteger.valueOf(numIds));
				
			
		BigInteger idValue = BigInteger.ZERO;
		for(long i = 0; i < numIds;i++){
			Identifier id = new BasicIdentifier(idValue,length);
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
		IdFileGenerationTask generator = new IdFileGenerationTask(args[0],
				Integer.valueOf(args[1]),
				Integer.valueOf(args[2]));
		
			generator.run();		

	}

}
