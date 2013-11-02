package edu.columbia.cs.event.annotations;

import java.io.File;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 11/1/13
 * Time: 6:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class Excerpt {

    // Source document of excerpt -- here this is the xml output of the StanfordCoreNLP processor with all annotators.
    private File source;

    // Indices of excerpt
    private List<Integer> sentenceIndices;

    // Optional label for this excerpt
    private String label = "";

    /**
     * Holds the sentence indices of an excerpt from a document <b>source</b>.
     * @param source xml file output from StanfordCoreNlp with all annotators enabled.
     * @param sentenceIndices of sentences to include in this excerpt ranging from 0 to n-1 where n is the number of
     *                        sentences in the document.
     */
    public Excerpt(File source, List<Integer> sentenceIndices) {
        this.source = source;
        this.sentenceIndices = sentenceIndices;
    }

    public File getSource() {
        return source;
    }

    public void setSource(File source) {
        this.source = source;
    }

    public List<Integer> getSentenceIndices() {
        return sentenceIndices;
    }

    public void setSentenceIndices(List<Integer> sentenceIndices) {
        this.sentenceIndices = sentenceIndices;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
