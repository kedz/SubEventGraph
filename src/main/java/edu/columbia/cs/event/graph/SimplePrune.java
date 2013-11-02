package edu.columbia.cs.event.graph;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 11/2/13
 * Time: 1:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class SimplePrune {


    public SimplePrune() {

    }


    public Graph pruneGraph(Graph<EventVertex, WeightedEdge> graph) {

        Graph<EventVertex,WeightedEdge> prunedGraph = new DirectedSparseMultigraph<EventVertex,WeightedEdge>();


        for (EventVertex e : graph.getVertices()) {

            double maxScore = 0;
            WeightedEdge maxEdge = null;
            EventVertex maxVertex = null;

            for (EventVertex p : graph.getPredecessors(e)) {

                WeightedEdge edge = graph.findEdge(p, e);

                double score = edge.getWeight();

                if (score > maxScore) {
                    maxScore = score;
                    maxVertex = p;
                    maxEdge = edge;
                }

            }

            if (maxScore > 0) {

                prunedGraph.addEdge(maxEdge, maxVertex, e, EdgeType.DIRECTED);

            }

        }

        return prunedGraph;
    }

}
