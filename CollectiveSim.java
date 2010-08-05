package edu.upc.cnds.collectivesim;

import edu.upc.cnds.collectivesim.experiment.Experiment;

public class CollectiveSim {
	private static 	InheritableThreadLocal<Experiment> experiment = new InheritableThreadLocal<Experiment>();

	public static Experiment getExperiment() {
		return experiment.get();
	}
	
	
	public static void setExperiment(Experiment e) {
		experiment.set(e);
	}

}
