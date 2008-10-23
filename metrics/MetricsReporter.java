package edu.upc.cnds.collectivesim.metrics;

public interface MetricsReporter {

    /*
     * Open
     * initializes the sink and prepares it to receive metrics
     */
    public void open();

    /*
     *  close
     *  Closes the sink and prevents further writes until it is 
     *  openned (@see open())
     */
    public void close();

    /*
     * write an array of bytes to the sink
     * @param timestamp time the metrics was captured (as reported by issuer)
     * @param agentName agent reporting metric
     * @param metric name of the metric
     * @param value value of the metric represented as a string
     */
    public void writeMetric(Metric metric);

}