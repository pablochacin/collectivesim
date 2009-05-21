package edu.upc.cnds.collectivesim.identifier;

import edu.upc.cnds.collectives.identifier.IdSpace;
import edu.upc.cnds.collectives.identifier.Identifier;
import edu.upc.cnds.collectivesim.stream.Stream;

/**
 * Generates a random stream of Identifiers from an IdSpace.
 * 
 * @author Pablo Chacin
 *
 */
public class IdRandomStream implements Stream<Identifier> {

	private String name;
	
	private IdSpace space;
	

	
	public IdRandomStream(String name, IdSpace space) {
		super();
		this.name = name;
		this.space = space;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Identifier getValue() {
		return space.getRandomIdentifier();
	}

	@Override
	public void reset() {
		//Do nothing. Reseting this stream has no effect
	}

	@Override
	public void open() {
		// Do nothing.
		
	}

}
