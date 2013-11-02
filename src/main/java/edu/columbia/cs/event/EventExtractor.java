package edu.columbia.cs.event;

import edu.columbia.cs.event.pipeline.TimeUnit;
import edu.columbia.cs.event.pipeline.component.PipelineComponent;
import edu.stanford.nlp.trees.Tree;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 10/26/13
 * Time: 6:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class EventExtractor implements PipelineComponent {


    public void init() {}

    public void processTimeUnit(TimeUnit timeUnit) {

        for (File xmlFile : timeUnit.getArticleMetaDirectory().listFiles()) {
            for(Tree tree : Slurp.parses(xmlFile))
                tree.pennPrint();
        }



    }



}
