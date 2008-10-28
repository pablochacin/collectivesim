package edu.upc.cnds.collectivesim.metrics;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;

import edu.upc.cnds.collectivesim.models.Model;
import edu.upc.cnds.collectivesim.models.imp.BasicModel;


/**
 * Reports metrics to independent files as comma delimeted values.
 * 
 * @author Pablo Chacin
 *
 */
public class MultifileMetricsReport implements MetricsReporter {


        
        /*
         * implements a null output stream to receive data that will be discarted.
         * Used as output stream for those output sinks that filed to open its output file.
         */
        private class NullOutputStream extends OutputStream {
            
            public void write(int b) {
                
            }
            
            public void write(byte[] data) {
                
            }
            
            public void write(byte[] data, int offset, int length) {
                
            }
            
        }
        
        // member variables
        private Model model;
        private String path;
        private String[] metrics;
        private HashMap metricsFiles;
        private HashSet ignoredMetrics;
        private boolean open;
        
        /*
         * Constructor. Creates a sink and initializes its output stream
         */
        public MultifileMetricsReport(BasicModel model){
            this.model = model;
            
            this.path = model.getStringProperty("Path");
            
            //get list of metrics
            String metricsList = model.getStringProperty("Metrics");
            if(metricsList != null){
                this.metrics = metricsList.split(",");    
            }else{
                metrics = new String[0];
            }
            
            metricsFiles = new HashMap();
            ignoredMetrics= new HashSet();
            open = false;
            
            
        }
        
        
        /*
         * Open
         * initializes the sink and prepares it to receive metrics
         */
        /* (non-Javadoc)
         * @see simrealms.metrics.MetricsReporter#open()
         */
        public void open(){
            open = true;
            
            //for each metric, create the output stream for the file
            OutputStream outStream;
            
            for (int i = 0;i < metrics.length;i++){
                try {
                    outStream = new FileOutputStream(path+"/"+metrics[i]+".txt");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    
                    outStream = new NullOutputStream();
                }
                
                metricsFiles.put(metrics[i],outStream);
            }
            
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
        public void writeMetric(Metric metric){
            OutputStream outStream;
            
            
            //find the correspoding output stream for the metric
            outStream = (OutputStream)metricsFiles.get(metric.getName());
            
            //if no file defined for metric
            if (outStream == null){
                //if the first time this metric is reported, warns that
                //no file was defined and put in a list to ignore future
                //report of this metric
                if(!ignoredMetrics.contains(metric.getName())){
                    System.err.println("No outfile defined for metric: "+ metric.getName());
                    ignoredMetrics.add(metric.getName());
                }
                return;             
            }
            
            
            //construct the metric record 
            String metricRecord = format(metric);
            
            // write metrics record to the corresponding file
            try {
                
                outStream.write(metricRecord.getBytes());
                
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Exception writing metrics to output Stream " + path);
                e.printStackTrace();
                
                //nullify the output stream to avoid repeating errors
                outStream = new NullOutputStream();
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
    }   
