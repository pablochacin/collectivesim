package edu.upc.cnds.collectivesim.grid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.upc.cnds.collectiveg.GridProcess;
import edu.upc.cnds.collectiveg.GridResource;
import edu.upc.cnds.collectiveg.GridTask;
import edu.upc.cnds.collectiveg.InvalidResourceEspecification;
import edu.upc.cnds.collectiveg.GridProcess.ExecutionState;
import edu.upc.cnds.collectiveg.GridLRM;
import edu.upc.cnds.collectiveg.GridLRM.POLICY;
import edu.upc.cnds.collectives.node.Node;
import edu.upc.cnds.collectivesim.model.ModelAgent;
import edu.upc.cnds.collectivesim.model.ModelException;
import edu.upc.cnds.collectivesim.model.Stream;
import edu.upc.cnds.collectivesim.model.imp.ReflexionModelAgent;


public class GridLrmAgent extends ReflexionModelAgent implements GridLRM{

	/**
	 * Description of the resources managed by this LRM
	 */
	protected GridResource resource;

	/**
	 * LRM's name in the grid. MUST be unique.
	 */
	protected String name;

	/**
	 * Execution policy
	 */
	protected POLICY policy;

	
	protected List<GridProcess>processQueue;
	
	private GridLrmModel model;
	
	private long lastUpdate;
	
	private Stream<Double>backgroundLoad;
	
	/**
	 * Node on which the lrm resided
	 */
	private Node localNode;
	
	public GridLrmAgent(Node localNode,GridLrmModel model,String name, GridResource resource, Stream<Double>backgroundLoad,POLICY policy) throws InvalidResourceEspecification {
		this.localNode = localNode;
		this.name = name;
		this.resource = resource;
		this.policy = policy;
		this.processQueue = new ArrayList<GridProcess>();
		this.model = model;
		this.backgroundLoad  = backgroundLoad;
		lastUpdate = model.getCurrentTime();
	}


	/**
	 * @return the Node on which this Lrm resides
	 */
	public Node getNode(){
		return localNode;
	}
	
	public long getCurrentTime() {
		return model.getCurrentTime();
	}


	/**
	 * Submits a task to the resource
	 * @param task
	 */
	public GridProcess sumbit(GridTask task){

		BasicGridProcess process = new BasicGridProcess(this,task);
		processQueue.add(process);
		return process;
	}

	
	public GridResource[] getResources(){
		GridResource[] resources = {resource};
		
		return resources;
	}
	
	/**
	 * Updates the execution state of the process
	 * 
	 */
	public void updateProcesses(){

		if(processQueue.size() == 0){
			return;
		}
				
		//calculate the execution slice for each job in the queue
		//based on the time they have been executing, the speed of the resource
		//the background load of the system (jobs not managed by the LRM) and the 
		//number of jobs being served
		long quantum = getCurrentTime()-lastUpdate;
		long slice = getQuantumSlice(quantum,getNumActiveProcesses());
		
		//update execution time		
		Iterator<GridProcess> i  = processQueue.subList(0, getNumActiveProcesses()).iterator();
		
		while(i.hasNext()){
			BasicGridProcess p = (BasicGridProcess)i.next();
			p.accountCpuTime(slice);
			if(p.getState().equals(ExecutionState.COMPLETED)){
				i.remove();
				model.reportProcessEnded(this,p);
			}
		}

		lastUpdate = getCurrentTime();
	}

	/**
	 * Returns the quantum slice that will receive each of the currently active processes
	 * 
	 * @param quantum time to be shared among processes
	 * @param numProcesses num of processes to share the quantum
	 * @return
	 */
	protected long getQuantumSlice(long quantum,int numProcesses){
		double cpuRate = resource.getSpeed()*(1-getBackgroundLoad());		
		long slice = (long)((quantum/Math.max(1, numProcesses))*cpuRate);
		return slice;
	
	}
	
	protected int getNumActiveProcesses(){
		//depending on the policy, all or just the first job
		//in the queue are active
		int numProcesses;
		if(policy.equals(POLICY.FCFS)){
			numProcesses= 1;
		}
		else{
			numProcesses = processQueue.size();
			
		}
		
		return numProcesses;
	}
	
	
	public GridProcess[] getActiveProcesses(){
		return processQueue.subList(0, getNumActiveProcesses()).toArray(new GridProcess[getNumActiveProcesses()]);
	}
	
	protected double getBackgroundLoad(){
		return backgroundLoad.getValue();
	}
	

	public Double getCurrentLoad() {
		throw new UnsupportedOperationException();
	}


	//TODO: calculate the service rate considering that in a FCFS policy
	//      some processes are not getting ANY processing time, so the average
	//      serviceRate should be lower than in a RR policy.
	public Double getServiceRate() {
		//calculate the service rate as the ratio among a given quantum and
		//the share received by each process
		return (double)(getQuantumSlice(100,processQueue.size()))/100.0;	
	}

	
	
}
