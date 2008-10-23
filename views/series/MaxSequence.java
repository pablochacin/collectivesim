package edu.upc.cnds.collectivesim.views.series;

import java.util.ArrayList;
import java.util.Collections;

import uchicago.src.sim.analysis.Sequence;
import uchicago.src.sim.analysis.StatisticUtilities;

public class MaxSequence implements Sequence {

    private ArrayList list;
    
    public MaxSequence(ArrayList list){
        this.list = list;
    }
    
    /**
     * Compute and return the maximun.
     */
    public double getSValue() {
        return ((Double)Collections.max(list)).doubleValue();
    }

}
