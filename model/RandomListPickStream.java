package edu.upc.cnds.collectivesim.model;

import java.util.List;

import java.util.Random;

public class RandomListPickStream<T> implements Stream<T> {


	private String name;
	
	private List<T>list;
	
	private Random rand;
	
	public RandomListPickStream(String name, List<T> list) {
		this.name = name;
		this.list = list;
		this.rand = new Random();
	}



	@Override
	public String getName() {
		return name;
	}



	@Override
	public T getValue() {
	 return list.get(rand.nextInt(list.size()));
	}

}
