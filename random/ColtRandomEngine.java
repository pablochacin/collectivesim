package edu.upc.cnds.collectivesim.random;

import java.util.Random;

import cern.jet.random.engine.RandomEngine;
import edu.upc.cnds.collectives.Collectives;

/**
 * Wraps the random number generator provided by the Experiment as a RandomEngine
 * needed by COLT library to generate number following diverse distributions.
 * 
 * @author Pablo Chacin
 *
 */
public class ColtRandomEngine extends RandomEngine {

	Random rand;
	
	public ColtRandomEngine() {
		rand = Collectives.getExperiment().getRandomGenerator();
	}
	
	@Override
	public int nextInt() {
		return rand.nextInt();
	}

}
