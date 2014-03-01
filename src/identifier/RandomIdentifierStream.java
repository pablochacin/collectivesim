package collectivesim.identifier;

import java.util.Random;

import cern.jet.random.engine.MersenneTwister64;
import collectives.Collectives;
import collectives.identifier.BasicIdentifier;
import collectives.identifier.IdSpace;
import collectives.identifier.Identifier;
import collectivesim.CollectiveSim;
import collectivesim.random.MersenneRandom;
import collectivesim.stream.Stream;

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
		this.rnd = CollectiveSim.getExperiment().getRandomGenerator();
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
