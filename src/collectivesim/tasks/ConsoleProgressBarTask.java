package collectivesim.tasks;

import java.util.Arrays;

import collectivesim.experiment.Experiment;

public class ConsoleProgressBarTask implements Runnable {

	protected Experiment experiment;
	
	protected static int DEFAULT_TICS = 20;
	
	protected int tics;
	
	
	public ConsoleProgressBarTask(Experiment experiment, int tics){
		this.experiment = experiment;
		this.tics = tics;
	}
	
	public ConsoleProgressBarTask(Experiment experiment){
		this(experiment,DEFAULT_TICS);
	}
	
	@Override
	public void run() {
		
		String progress = getProgressBar(tics,
				                         experiment.getRunLength(),
				                         experiment.getScheduler().getTime());

		
		printProgressBar(progress);
		
	}
	
	protected String getProgressBar(int tics,long total,long completed) {
        
        int progress = Math.round((completed * tics) / total);
        String totalStr = String.valueOf(total);
        String percent = String.format("%0"+totalStr.length()+"d/%s [", completed, totalStr);
        String progressBar= percent + repeatChar('=', progress)+ repeatChar(' ', 20 - progress) + "]";
        
        return progressBar;
	}	
	
	protected void printProgressBar(String progress){
		System.out.print('\r');
		System.out.print(progress);
	}
	
	/**
	 * Returns a String with a repeated character.
	 * Amazing, but Java doesn't offers this!
	 * @param c
	 * @param length
	 * @return
	 */
	protected String repeatChar(char c,int length){
		char[]array = new char[length];
		Arrays.fill(array, c);
		return new String(array);
	}

}
