package edu.columbia.cs.event.graph;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 11/2/13
 * Time: 2:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class WeightedEdge {

    private double weight;

    public WeightedEdge(double weight) {
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String toString() {
        return String.format("E: %f", weight);
    }
}
