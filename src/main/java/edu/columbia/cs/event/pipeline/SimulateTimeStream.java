package edu.columbia.cs.event.pipeline;

import org.joda.time.DateTime;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 9/14/13
 * Time: 4:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class SimulateTimeStream {

    public static void main(String[] args) {

        PipelineUtil utils = null;

        if(args.length == 1) {
            utils = new PipelineUtil( new File(args[0]) );
        } else {
            utils = new PipelineUtil(ClassLoader.getSystemClassLoader().getResourceAsStream("modeler.properties"));
        }

        //TimeUnit timeUnit = new TimeUnit(DateTime.parse("2012-10-22"), utils);
        List<TimeUnit> inputList = new LinkedList<TimeUnit>();

        TimeUnit day1 = new TimeUnit(DateTime.parse("2012-10-22"), utils);

        TimeUnit day2 = new TimeUnit(DateTime.parse("2012-10-23"), utils);
        day2.setPreviousTimeUnit(day1);


        TimeUnit day3 = new TimeUnit(DateTime.parse("2012-10-24"), utils);
        day3.setPreviousTimeUnit(day2);


        TimeUnit day4 = new TimeUnit(DateTime.parse("2012-10-25"), utils);
        day4.setPreviousTimeUnit(day3);
        /*
        TimeUnit day5 = new TimeUnit(DateTime.parse("2012-10-26"), utils);
        day5.setPreviousTimeUnit(day4);

        TimeUnit day6 = new TimeUnit(DateTime.parse("2012-10-27"), utils);
        day6.setPreviousTimeUnit(day5);

        TimeUnit day7 = new TimeUnit(DateTime.parse("2012-10-28"), utils);
        day7.setPreviousTimeUnit(day6);


        */

        TimeUnit day8 = new TimeUnit(DateTime.parse("2012-10-29"), utils);
        //day8.setPreviousTimeUnit(day7);


        inputList.add(day1);
        //inputList.add(day2);
        //inputList.add(day3);
        //inputList.add(day4);

        //inputList.add(day5);
        //inputList.add(day6);
        //inputList.add(day7);
        //inputList.add(day8);




        List<String> components = new LinkedList<String>();


        /*
        components.add("edu.columbia.cs.subevent.component.RawTextExtractor");

        components.add("edu.columbia.cs.subevent.component.NLPStack");
        components.add("edu.columbia.cs.subevent.component.DiscourseAnnotator");
        */
        //components.add("edu.columbia.cs.subevent.component.EntityExtractor");

        //components.add("edu.columbia.cs.subevent.component.ParagraphAnnotator2");

        //components.add("edu.columbia.cs.subevent.component.SimpleTopicModeler");
        //components.add("edu.columbia.cs.subevent.component.SimpleClusterOnTopics");
        //components.add("edu.columbia.cs.subevent.component.EventExtractionComponent");
        //components.add("edu.columbia.cs.subevent.component.EventClusterAnnotationBuilder");
        //components.add("EventExtractor");
        //components.add("CoreNlpComponent");

        components.add("edu.columbia.cs.event.pipeline.component.ParagraphAnnotator");
        components.add("edu.columbia.cs.event.pipeline.component.SingleExcerptCluster");
        components.add("edu.columbia.cs.event.pipeline.component.GraphBuilder");



        SubEventPipeline pipeline = new SubEventPipeline(components);
        pipeline.init();

        for (TimeUnit timeUnit : inputList)
            pipeline.start(timeUnit);



    }


}
