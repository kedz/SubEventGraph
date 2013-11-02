package edu.columbia.cs.event.graph.scorer;

import edu.columbia.cs.event.graph.EventVertex;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 11/2/13
 * Time: 3:00 PM
 * To change this template use File | Settings | File Templates.
 */
public interface EdgeScorer {

    public double score(EventVertex e1, EventVertex e2);


}
