package edu.upc.cnds.collectivesim.identifier;

import java.util.Random;

import cern.jet.random.engine.MersenneTwister64;
import edu.upc.cnds.collectives.Collectives;
import edu.upc.cnds.collectives.identifier.BasicIdentifier;
import edu.upc.cnds.collectives.identifier.IdSpace;
import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectivesim.random.MersenneRandom;
import edu.upc.cnds.collectivesim.stream.Stream;

/**
 * Generates a random stream of Identifiers from an IdSpace.
 * 
 * @author Pablo Chacin
 *
 */
public class RandomIdentifierStream implements Stream<Identifier> {

	private String name;
	

	private int size;
	
	private Random rnd; 

	
	public RandomIdentifierStream(String name,int size) {
		super();
		this.name = name;
		this.size = size;
		this.rnd = Collectives.getExperiment().getRandomGenerator();
	}

	@Override
	public String getName() {
		return name;
	}



	@Override
	public void reset() {
		//Do nothing. Reseting this stream has no effect
	}

	@Override
	public void open() {
		// Do nothing.
		
	}

	@Override
	public boolean hasMoreElements() {
		return true;
	}

	@Override
	public Identifier nextElement() {
		byte[] bytes = new byte[size];
		
		rnd.nextBytes(bytes);
		
		return new BasicIdentifier(bytes);
	}

}
