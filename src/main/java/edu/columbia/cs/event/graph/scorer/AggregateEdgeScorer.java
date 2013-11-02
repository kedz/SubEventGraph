package edu.columbia.cs.event.graph.scorer;

import edu.columbia.cs.event.graph.EventVertex;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 11/2/13
 * Time: 3:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class AggregateEdgeScorer implements EdgeScorer {

    private List<EdgeScorer> edgeScorers = new LinkedList<EdgeScorer>();

    public AggregateEdgeScorer() {}

    public void addEdgeScorer(EdgeScorer scorer) {
        edgeScorers.add(scorer);
    }

    public double score(EventVertex e1, EventVertex e2) {

        double score = 0.0;

        for (EdgeScorer scorer : edgeScorers)
            score += scorer.score(e1, e2);

        return score;

    }

}
