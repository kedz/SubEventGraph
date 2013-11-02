package edu.columbia.cs.event.pipeline;

import cc.mallet.topics.ParallelTopicModel;
import edu.columbia.cs.event.annotations.Cluster;
import edu.columbia.cs.event.annotations.Excerpt;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 9/14/13
 * Time: 7:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class TimeUnit {

    private String processUnitId;


    private File articleDir;
    private File rawTxtDir;
    private File articleMetaDir;
    private File articleDiscourseAnnotationDir;
    private File graffDir;
    private File graffAnntnDir;
    private File topicAssignmentDir;
    private File clusterAssignmentDir;
    private File entitiesDir;
    private File eventExtractFile;
    private File clusterDir;
    private File eventModelReportDir;



    private int numDocs;
    private DateTime dateTime;
    private DateTimeFormatter fmt;
    private ParallelTopicModel topicModel = null;
    private TimeUnit previousTimeUnit;

    // Output from ParagraphAnnotator
    private List<Excerpt> excerpts = new LinkedList<Excerpt>();

    // Output from clustering components
    private List<Cluster> clusters = new LinkedList<Cluster>();

    public TimeUnit(DateTime dateTime, PipelineUtil pipelineUtil, DateTimeFormatter fmt) {

        this.dateTime = dateTime;
        this.fmt = fmt;
        processUnitId = dateTime.toString(fmt);

        this.articleDir = new File( pipelineUtil.getArticleDirectory(), processUnitId);
        this.rawTxtDir = new File( pipelineUtil.getRawTextDirectory(), processUnitId);
        this.graffDir = new File( pipelineUtil.getParagraphDirectory(), processUnitId);
        this.graffAnntnDir = new File( pipelineUtil.getParagraphAnnotationDirectory(), processUnitId);
        this.articleMetaDir = new File( pipelineUtil.getArticleMetaDirectory(), processUnitId);
        this.articleDiscourseAnnotationDir = new File( pipelineUtil.getArticleDiscourseAnnotationDirectory(), processUnitId);
        this.topicAssignmentDir = new File( pipelineUtil.getTopicAssignmentDirectory(), processUnitId);
        this.clusterAssignmentDir = new File( pipelineUtil.getClusterAssignmentDirectory(), processUnitId);
        this.entitiesDir = new File( pipelineUtil.getEntitiesDirectory(), processUnitId);
        this.eventExtractFile = new File( pipelineUtil.getEventExtractsDirectory(), processUnitId+".evt");
        this.clusterDir = new File( pipelineUtil.getClusterDirectory(), processUnitId);
        this.numDocs = getArticleDirectory().listFiles().length;
        this.eventModelReportDir = new File( pipelineUtil.getEventModelReportDirectory(), processUnitId);
        //this.numDocs = graffDir.listFiles().length;

        //for( File source : graffDir.listFiles() ) {
        //    fileId2TextUnit.put(source.toString(), new TextUnit(source.toString()));
        //}
    }


    public String getProcessUnitId() {
        return processUnitId;
    }

    public TimeUnit(DateTime dateTime, PipelineUtil pipelineUtil) {
        this(dateTime, pipelineUtil, DateTimeFormat.forPattern("YYYY-MM-dd"));
    }

    public File getArticleDirectory() {
        return articleDir;
    }

    public File getRawTextDirectory() {
        return rawTxtDir;
    }

    public File getArticleMetaDirectory() {
        return articleMetaDir;
    }

    public File getArticleDiscourseAnnotationDirectory() {
        return articleDiscourseAnnotationDir;
    }

    public File getParagraphDirectory() {
        return graffDir;
    }

    public File getParagraphAnnotationDirectory() {
        return graffAnntnDir;
    }

    public File getEntitiesDirectory() {
        return entitiesDir;
    }

    public File getTopicAssignmentDirectory() {
        return topicAssignmentDir;
    }

    public File getClusterAssignmentDirectory() {
        return clusterAssignmentDir;
    }

    public File getEventExtractFile() {
        return eventExtractFile;
    }

    public File getClusterDirectory() {
        return clusterDir;
    }

    public int numDocs() {
        return numDocs;
    }

    public void setTopicModel(ParallelTopicModel topicModel) {this.topicModel = topicModel;}
    public ParallelTopicModel getTopicModel() {return topicModel;}



    public File getEventModelReportDirectory() {
        return eventModelReportDir;
    }

    public void setPreviousTimeUnit(TimeUnit previousTimeUnit) {
        this.previousTimeUnit = previousTimeUnit;
    }

    public TimeUnit getPreviousTimeUnit() {
        return previousTimeUnit;
    }

    public List<Excerpt> getExcerpts() {
        return excerpts;
    }

    public void setExcerpts(List<Excerpt> excerpts) {
        this.excerpts = excerpts;
    }

    public List<Cluster> getClusters() {
        return clusters;
    }

    public void setClusters(List<Cluster> clusters) {
        this.clusters = clusters;
    }
}
