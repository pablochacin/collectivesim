package edu.upc.cnds.collectivesim.underlay.Space2D;

import java.util.Random;

import edu.upc.cnds.collectives.underlay.UnderlayNode;
import edu.upc.cnds.collectivesim.random.MersenneRandom;

public class RandomSpaceLocationStrategy implements SpaceLocationStrategy {

	private Random random = new MersenneRandom();
	
	@Override
	public Space2DLocation getLocation(Space2DTopology topology,UnderlayNode node) {
		
		return new Space2DLocation(topology, node,random.nextDouble()*topology.getSizeX(),
								   random.nextDouble()*topology.getSizeY());
	}

}
