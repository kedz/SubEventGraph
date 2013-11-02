package edu.columbia.cs.event.pipeline.component;

import edu.columbia.cs.event.Slurp;
import edu.columbia.cs.event.pipeline.TimeUnit;
import edu.columbia.cs.event.annotations.Cluster;
import edu.columbia.cs.event.annotations.Excerpt;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 11/1/13
 * Time: 7:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class SingleExcerptCluster implements PipelineComponent {

    public void init() {}

    public void processTimeUnit(TimeUnit timeUnit) {

        int numExcerpts = timeUnit.getExcerpts().size();
        List<Cluster> clusters = new ArrayList<Cluster>(numExcerpts);
        for (Excerpt excerpt : timeUnit.getExcerpts()) {
            Cluster cluster = new Cluster();
            cluster.getExcerpts().add(excerpt);


            String label = String.format("%s: %s",
                    excerpt.getLabel(),
                    Slurp.sentence(excerpt.getSource(), excerpt.getSentenceIndices().get(0)));

            cluster.setLabel(label);
            clusters.add(cluster);
        }

        timeUnit.setClusters(clusters);


    }



}
