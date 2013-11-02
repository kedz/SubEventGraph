package edu.columbia.cs.event.pipeline.component;

import edu.columbia.cs.event.pipeline.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 9/14/13
 * Time: 7:44 PM
 * To change this template use File | Settings | File Templates.
 */
public interface PipelineComponent {


    //public void msg();
    public void init();

    public void processTimeUnit(TimeUnit timeUnit);

}
