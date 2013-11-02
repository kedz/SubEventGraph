package edu.columbia.cs.event.pipeline;

import edu.columbia.cs.event.pipeline.component.PipelineComponent;
import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.dictionary.Dictionary;
import org.joda.time.DateTime;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 9/14/13
 * Time: 4:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class SubEventPipeline {

    private static Dictionary wn;

    private List<String> componentClassNames;
    private List<PipelineComponent> components = new LinkedList<PipelineComponent>();

    public SubEventPipeline(List componentClassNames) {
        this.componentClassNames = componentClassNames;
    }


    public void init() {

        try {
            wn = Dictionary.getInstance(new FileInputStream("/home/chris/projects/event/file_properties.xml"));
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (JWNLException jwnle) {
            jwnle.printStackTrace();
        }

        System.out.println("Initializing SubEvent Detection Pipeline...");
        for(String componentName : componentClassNames) {
            System.out.println("\tInitializing " + componentName + "...");

            try {
                Class theClass  = Class.forName(componentName);
                PipelineComponent component = (PipelineComponent)theClass.newInstance();
                component.init();
                components.add(component);
                System.out.println("\t\tComponent initialized.");

            } catch (ClassNotFoundException ex ){
                System.err.println( ex + " class must be in class path.");
            } catch (InstantiationException ex ){
                System.err.println( ex + " class must be concrete.");
            }
            catch( IllegalAccessException ex ){
                System.err.println( ex + " class must have a no-arg constructor.");
            }


        }



    }



    public void start(TimeUnit timeUnit) {

        for(PipelineComponent component : components) {
            System.out.println(DateTime.now()+" : TIME UNIT: "
                    + timeUnit.getProcessUnitId()
                    + " -  ENTERING "+component.getClass().getName()
                    + "\n");

            component.processTimeUnit(timeUnit);

            System.out.println(DateTime.now()+" : TIME UNIT: "
                    + timeUnit.getProcessUnitId()
                    + " -  LEAVING "+component.getClass().getName()
                    + "\n");

        }



    }

    public static void logTabbed(String logMsg) {
        System.out.println(DateTime.now() + " : \t" + logMsg);
    }

    public static Dictionary getWordNet() {
        return wn;
    }

}
