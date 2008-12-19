package edu.upc.cnds.collectivesim.metrics;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import edu.upc.cnds.collectives.events.Event;
import edu.upc.cnds.collectives.events.EventFilter;
import edu.upc.cnds.collectives.events.EventReporter;
import edu.upc.cnds.collectives.metrics.Metric;
import edu.upc.cnds.collectivesim.scheduler.Scheduler;
import edu.upc.cnds.collectivesim.scheduler.repast.RepastScheduler;


/**
 * Reports metrics to independent files as comma delimeted values.
 * 
 * @author Pablo Chacin
 *
 */
public class MultifileMetricsReport implements EventReporter {

	/**
	 * A dummy filter that always return true, used when no filter is set. 
	 */
	 private class DummyFilter implements EventFilter {

		public boolean filter(Event event) {
			return true;
		}
		 
	 }
        
	 	/**
	 	 * Path to the event files
	 	 */
	 	private String path;
	 	
	 	/**
	 	 * Map with the output streams 
	 	 */
        private Map<String,OutputStream> metricsFiles;
        
        
        private EventFilter filter;
        
        /*
         * Constructor. Creates a sink and initializes its output stream
         */
        public MultifileMetricsReport(String path){
            this.filter = new DummyFilter();
            
            this.path = path;
                      
            metricsFiles = new HashMap<String,OutputStream>();
                        
        }
        
        

        private OutputStream getOutputStream(String name) {
        	
        	OutputStream outStream = metricsFiles.get(name);
        	
            try {
                outStream = new FileOutputStream(path+"/"+name+".txt");
            } catch (FileNotFoundException e) {

            }
            
            metricsFiles.put(name,outStream);
            
                        
            return outStream;
        }

    
        
        /*
         *  close
         *  Closes the sink and prevents further writes until it is 
         *  openned (@see open())
         */
        /* (non-Javadoc)
         * @see simrealms.metrics.MetricsReporter#close()
         */
        public void close(){
            
            //if sink is not open, do nothing
            //TODO rise exception
            if (!open){
                return;
            }
            
            OutputStream outStream;
            
            for (int i = 0;i < metrics.length;i++){
                outStream = (OutputStream)metricsFiles.get(metrics[i]);
                try {
                    outStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                   System.err.println("Exception opening report file: "+metrics[i]);
                   e.printStackTrace();
                }
            }
        }
        
        /*
         * write an array of bytes to the sink
         * @param timestamp time the metrics was captured (as reported by issuer)
         * @param agentName agent reporting metric
         * @param metric name of the metric
         * @param value value of the metric represented as a string
         */
        /* (non-Javadoc)
         * @see simrealms.metrics.MetricsReporter#writeMetric(simrealms.metrics.Metric)
         */
        public void fireEvent(Event metric){
        	
            OutputStream outStream;
            
            
            //find the correspoding output stream for the metric
            outStream = (OutputStream)metricsFiles.get(metric.getType().getName());
            
 
            
            //construct the metric record 
            String metricRecord = format((Metric)metric);
            
            // write metrics record to the corresponding file
            try {
                
                outStream.write(metricRecord.getBytes());
                
            } catch (IOException e) {
                e.printStackTrace();
              log.error("Exception writing metrics to output Stream " + path);
                e.printStackTrace();
                
            }
            
            
            
        }
        
        /*
         * Method that formats the metric in a String
         * 
         * 
         */
        private String format(Metric metric){
            String metricRecord = "";
            
                   
            //format record
            metricRecord += metric.getTimestamp();
            metricRecord += ","+metric.getName() + "," + metric.getValue();
            metricRecord += "\n";
            
            return metricRecord;
            
        }




		public void setFilter(EventFilter filter) {
			this.filter = filter;
			
		}
    }   
