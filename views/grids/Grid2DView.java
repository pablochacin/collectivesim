package edu.upc.cnds.collectivesim.views.grids;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import edu.upc.cnds.collectivesim.models.Action;
import edu.upc.cnds.collectivesim.models.BasicModel;
import edu.upc.cnds.collectivesim.models.Model;
import edu.upc.cnds.collectivesim.topology.grid2d.Grid2D;
import edu.upc.cnds.collectivesim.views.View;
import edu.upc.cnds.collectivesim.views.grids.CellViewer;

import uchicago.src.collection.BaseMatrix;
import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.gui.Displayable;
import uchicago.src.sim.gui.Object2DDisplay;
import uchicago.src.sim.space.Discrete2DSpace;

public class Grid2DView implements View, Discrete2DSpace,BaseMatrix  {

    
    /**
     * 2d displays 
     */
    private Object2DDisplay display; 

    /**
     * Display surface used to display views
     */
    private DisplaySurface displaySurf;

    /**
     * Model
     */
    private Model model;
    
        
    /**
     * Viewer of each cell
     */
    private CellViewer cellViewer;

     
    /**
     *  space to be displayed 
     */
    Grid2D space;
    
    
    /**
     * Title
     */
    private String title;

    /**
     * Default constructor
     */
    public Grid2DView(BasicModel model,Grid2D space,CellViewer cellViewer,String title,long frequency){

        this.cellViewer = cellViewer;
        this.model = model;
        this.space = space;
        this.title = title;
        this.displaySurf = new DisplaySurface(model,title);
        
        //create a 2D grid for the realm and pass itself as an Object2DDisplay
        display = new Object2DDisplay(this);
        //display.setObjectList(space.getLocations());

        displaySurf.addDisplayableProbeable(display, title);
        
        //register the display surface to be updated
         model.registerDisplaySurface(title,displaySurf);
         
         //create an action to schedule the refreshment
         Action  refresh = new Action(this,"refresh",frequency,true);
         model.scheduleAction(refresh);
    }



    public void display() {

        displaySurf.display();
    }

    public void dispose() {

        if (displaySurf != null){
            displaySurf.dispose();
        }
        displaySurf = null;
    }


    public String getTitle() {
         return this.title;
    }


    /**
     * 
     */
    public void refresh(){
        this.displaySurf.updateDisplay();
    }


    public void setCellViewer(CellViewer cellViewer){
        this.cellViewer = cellViewer; 

    }


    //*********************************************************************
    // METHODS FROM Discrete2DSpace 
    //*********************************************************************
    public BaseMatrix getMatrix() {
        return this;
    }


    public Object getObjectAt(int x, int y) {

        cellViewer.setCell(this.space.getCell(x, y));
        return cellViewer;
    }


    public Dimension getSize() {
        return new Dimension(space.getSizeX(),space.getSizeY());
    }


    public int getSizeX() {
        return space.getSizeX();
    }


    public int getSizeY() {
        return space.getSizeY();
    }


    public double getValueAt(int x, int y) {
        throw new UnsupportedOperationException();
    }


    public void putObjectAt(int x, int y, Object object) {
        throw new UnsupportedOperationException();

    }


    public void putValueAt(int x, int y, double value) {
        throw new UnsupportedOperationException();

    }

//  *******************************************************************
//  METHODS FROM BaseMatrix
//  *******************************************************************

    public Object get(int col, int row) {
         return getObjectAt(col, row);
    }


    public int getNumCols() {
        return space.getSizeX();
    }


    public int getNumRows() {
        return space.getSizeY();
    }


    public void put(int col, int row, Object obj) {
        throw new UnsupportedOperationException();
    }


    public Object remove(int col, int row) {
        throw new UnsupportedOperationException();
    }


    public int size() {
        return space.getSizeX();
    }


    public void trim() {
        throw new UnsupportedOperationException();

    }
}
