package edu.columbia.cs.event.graph.scorer;

import edu.columbia.cs.event.Slurp;
import edu.columbia.cs.event.annotations.Excerpt;
import edu.columbia.cs.event.graph.EventVertex;
import edu.columbia.cs.event.pipeline.SubEventPipeline;
import javafx.geometry.Pos;
import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.data.PointerType;
import net.sf.extjwnl.data.relationship.AsymmetricRelationship;
import net.sf.extjwnl.data.relationship.RelationshipFinder;
import net.sf.extjwnl.data.relationship.RelationshipList;
import net.sf.extjwnl.dictionary.Dictionary;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 11/1/13
 * Time: 8:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class WordNetSubsumption implements EdgeScorer {


    private Pattern tagPattern;

    private POS pos;

    private boolean normalized = false;

    public WordNetSubsumption(POS pos, String tagPattern) {
        this.pos = pos;
        this.tagPattern = Pattern.compile(tagPattern);
    }


    public double score(EventVertex e1, EventVertex e2) {

        List<String> e1Lemmas = extractLemmas(e1);
        List<String> e2Lemmas = extractLemmas(e2);

        if (e1Lemmas.size() == 0 || e2Lemmas.size() == 0)
            return 0.0;

        Dictionary wn = SubEventPipeline.getWordNet();

        double edgeScore = 0;

        for (String verb : e1Lemmas) {
            for (String otherVerb : e2Lemmas)
                edgeScore += subsumptionCount(verb, otherVerb);
        }

        if (isNormalized())
            edgeScore = edgeScore / ((double)(e1Lemmas.size() * e2Lemmas.size()));

        return edgeScore;
    }

    public List<String> extractLemmas(EventVertex e) {



        List<String> verbs = new LinkedList<String>();

        for (Excerpt excerpt : e.getCluster().getExcerpts()) {

            for (String verb : Slurp.lemmasWithPosRegEx(excerpt, tagPattern))
                verbs.add(verb);
        }

        return verbs;
    }

    private int subsumptionCount(String word, String otherWord) {

        int edgeScore = 0;

        if (word.equals(otherWord))
            return edgeScore;

        Dictionary wn = SubEventPipeline.getWordNet();
        try {

            IndexWord iVerb = wn.getIndexWord(pos, word);
            IndexWord iOtherVerb = wn.getIndexWord(pos, otherWord);

            if (iVerb != null && iOtherVerb != null) {

                RelationshipList list = RelationshipFinder.findRelationships(iVerb.getSenses().get(0), iOtherVerb.getSenses().get(0), PointerType.HYPERNYM);
                if (list.size() > 0) {
                    //System.out.println("Hypernym relationship between \"" + iVerb.getLemma() + "\" and \"" + iOtherVerb.getLemma() + "\":");

                    //System.out.println("Common Parent Index: " + ((AsymmetricRelationship) list.get(0)).getCommonParentIndex());
                    //System.out.println("Relative target distance: " +((AsymmetricRelationship) list.get(0)).getRelativeTargetDepth());
                    //System.out.println("Depth: " + list.get(0).getDepth());
                    edgeScore += ((AsymmetricRelationship) list.get(0)).getRelativeTargetDepth();

                }

            }

        } catch (JWNLException jwnle) {
            jwnle.printStackTrace();
        } catch (CloneNotSupportedException cnse) {
            cnse.printStackTrace();
        }

        return edgeScore;

    }


    public boolean isNormalized() {
        return normalized;
    }

    public void setNormalized(boolean normalized) {
        this.normalized = normalized;
    }
}
