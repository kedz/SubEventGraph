package edu.columbia.cs.event.annotations;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 11/1/13
 * Time: 7:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class Cluster {

    private String label = "";

    private List<Excerpt> excerpts = new LinkedList<Excerpt>();

    public Cluster() {}

    public List<Excerpt> getExcerpts() {
        return excerpts;
    }

    public void setExcerpts(List<Excerpt> excerpts) {
        this.excerpts = excerpts;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
