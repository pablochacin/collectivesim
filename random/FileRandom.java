package edu.upc.cnds.collectivesim.random;


import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;


public class FileRandom extends Random {

	protected static String DEFAULT_RAND_FILE = "rand.dat";
	
	protected DataInputStream randFile;
			
	public FileRandom() {
		
		this(DEFAULT_RAND_FILE);
	}
	

	public FileRandom(String file) {
		try {
			randFile = new DataInputStream(new FileInputStream("rand.dat"));
		} catch (FileNotFoundException e) {
			throw new Error("unable to read random numbers file");
		} 
	}

	@Override
	protected int next(int nbits){
		
		try {
			
			int randInt = randFile.readInt();
			
			randInt &= ((1L << nbits) -1);
		    return (int) randInt;
			
		} catch (IOException e) {
			throw new Error("unable to read from random file:" + e.toString());
		}
		
	
	}
	
}
