package edu.upc.cnds.collectivesim.underlay.Space2D;

import java.util.Random;

import edu.upc.cnds.collectives.Collectives;
import edu.upc.cnds.collectives.underlay.UnderlayNode;

public class RandomSpaceLocationStrategy implements SpaceLocationStrategy {

	private Random random;
	
	public RandomSpaceLocationStrategy() {
		random = Collectives.getExperiment().getRandomGenerator();
	}
	
	
	@Override
	public Space2DLocation getLocation(Space2DTopology topology,UnderlayNode node) {
		
		return new Space2DLocation(topology, node,random.nextDouble()*topology.getSizeX(),
								   random.nextDouble()*topology.getSizeY());
	}

}
