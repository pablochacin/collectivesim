package edu.upc.cnds.collectivesim.views.series;

import java.util.ArrayList;
import java.util.Collections;

import uchicago.src.sim.analysis.Sequence;
import uchicago.src.sim.analysis.StatisticUtilities;

public class MinSequence implements Sequence {

    private ArrayList list;
    
    public MinSequence(ArrayList list){
        this.list = list;
    }
    
    /**
     * Compute and return the average.
     */
    public double getSValue() {
        return ((Double)Collections.min(list)).doubleValue();
    }

}
