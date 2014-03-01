package collectivesim.random;

import java.util.Random;

import cern.jet.random.engine.MersenneTwister64;

public class MersenneRandom extends Random {

	protected MersenneTwister64 engine;
	
	
	public MersenneRandom() {
		this.engine = new MersenneTwister64();
	}
	
	public MersenneRandom(int seed) {
		this.engine = new MersenneTwister64(seed);
	}


	@Override
	protected int next(int nbits){
		int randInt = engine.nextInt();
		
		randInt &= ((1L << nbits) -1);
	    return (int) randInt;
		
	}
	
}
