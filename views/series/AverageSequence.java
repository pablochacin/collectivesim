package edu.upc.cnds.collectivesim.views.series;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import uchicago.src.sim.analysis.Sequence;
import uchicago.src.sim.analysis.StatisticUtilities;

public class AverageSequence implements Sequence {

    private ArrayList list;
    
    public AverageSequence(ArrayList list){
        this.list = list;
    }
    
    /**
     * Compute and return the average
     */
    public double getSValue() {
        Iterator i = list.iterator();
        double sum = 0;
        while(i.hasNext()){
            sum += ((Double)i.next()).doubleValue();
        }
        
        return sum/list.size();
    }

}
