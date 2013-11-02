package edu.columbia.cs.event.graph;

import edu.columbia.cs.event.annotations.Cluster;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 11/1/13
 * Time: 7:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class EventVertex {

    private String label = "";
    private Cluster cluster;

    public EventVertex() {}

    public EventVertex(Cluster cluster) {
        this.cluster = cluster;
        this.label = cluster.getLabel();
    }

    public String toString() {
        return label;
    }

    public Cluster getCluster() {
        return cluster;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}
