package edu.columbia.cs.event.pipeline;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 9/20/13
 * Time: 6:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class PipelineUtil {

    private Properties props;

    public PipelineUtil(File propsFile) {
        props = new Properties();
        try {
            props.load(new FileReader(propsFile));
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(-1);
        }
    }

    public PipelineUtil(InputStream is) {
        props = new Properties();
        try {
            props.load(is);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(-1);
        }
    }

    public File getHomeDirectory() {
        return new File( props.getProperty("root.dir") );
    }

    public File getArticleDirectory() {
        return new File( getHomeDirectory(), props.getProperty("article.xml.dir") );
    }

    public File getRawTextDirectory() {
        return new File( getHomeDirectory(), props.getProperty("article.raw.txt.dir") );
    }


    public File getArticleMetaDirectory() {
        return new File( getHomeDirectory(), props.getProperty("article.meta.xml.dir") );
    }

    public File getArticleDiscourseAnnotationDirectory() {
        return new File(getHomeDirectory(), props.getProperty("article.discourse.antn.dir"));
    }

    public File getParagraphDirectory() {
        return new File( props.getProperty("root.dir")
                + File.separator
                + props.getProperty("graff.dir") );
    }

    public File getParagraphAnnotationDirectory() {
        return new File( getHomeDirectory(), props.getProperty("graff.antn.dir") );
    }

    public File getTopicAssignmentDirectory() {
        return new File( props.getProperty("root.dir")
                + File.separator
                + props.getProperty("topic.assignment.dir") );
    }

    public File getClusterAssignmentDirectory() {
        return new File( props.getProperty("root.dir")
                + File.separator
                + props.getProperty("cluster.assignment.dir") );
    }

    public File getEntitiesDirectory() {
        return new File( getHomeDirectory(), props.getProperty("entities.dir"));
    }


    public File getEventExtractsDirectory() {
        return new File( getHomeDirectory(), props.getProperty("article.event.extracts"));
    }

    public File getClusterDirectory() {
        return new File( getHomeDirectory(), props.getProperty("cluster.dir"));
    }

    public File getEventModelReportDirectory() {
        return new File( getHomeDirectory(), props.getProperty("eventmodel.report"));
    }

}
