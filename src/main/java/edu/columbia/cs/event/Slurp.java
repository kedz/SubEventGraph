package edu.columbia.cs.event;

import edu.columbia.cs.event.CachedFileIO;
import edu.columbia.cs.event.annotations.Excerpt;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.StringLabelFactory;
import edu.stanford.nlp.trees.LabeledScoredTreeFactory;
import edu.stanford.nlp.trees.PennTreeReader;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeReader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 10/26/13
 * Time: 6:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class Slurp {

    private static CachedFileIO cache = CachedFileIO.getCacheFileIO();

    private static Pattern extractRawPattern = Pattern.compile("<raw>(.*)<\\/raw>",Pattern.MULTILINE|Pattern.DOTALL);

    private static Pattern extractParagraphPattern = Pattern.compile("(.*?)\\n{2,}", Pattern.MULTILINE | Pattern.DOTALL);

    public static Tree[] parses(File coreNlpXml) {

        List<Tree> forest = new ArrayList<Tree>();
        Document doc = cache.readFile(coreNlpXml);
        NodeList parses = doc.getElementsByTagName("parse");
        for (int p = 0; p < parses.getLength(); p++) {

            String parseStr = parses.item(p).getTextContent();

            try {

                TreeReader r = new PennTreeReader(new StringReader(parseStr), new LabeledScoredTreeFactory(new StringLabelFactory()));
                Tree t = r.readTree();
                forest.add(t);

            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.exit(-1);
            }

        }

        return forest.toArray(new Tree[forest.size()]);
    }

    public static String rawFromArticleXml(File articleXml) {

        String rawText = "";

        try {
            Matcher m = extractRawPattern.matcher( IOUtils.slurpFile(articleXml) );

            if( m.find() ) {
                rawText = m.group(1);
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return rawText;

    }

    public static String[] rawParagraphsFromArticleXml(File articleFile) {

        List<String> graffList = new ArrayList<String>();

        String rawText = rawFromArticleXml(articleFile);

        Matcher m = extractParagraphPattern.matcher(rawText);
        while(m.find())
            graffList.add(m.group(1));

        String[] graffArray = new String[graffList.size()];

        return graffList.toArray(graffArray);

    }

    public static String sentence(File coreNlpXml, Integer sentenceNumber) {
        Document doc = cache.readFile(coreNlpXml);
        NodeList sentences = doc.getElementsByTagName("sentence");

        Element sentence = (Element) sentences.item(sentenceNumber);
        StringBuilder builder = new StringBuilder();
        NodeList tokens = sentence.getElementsByTagName("word");
        for (int t = 0; t < tokens.getLength(); t++)
            builder.append(tokens.item(t).getTextContent()+" ");

        return builder.toString().trim();
    }

    public static String[] lemmasWithPosRegEx(Excerpt e, Pattern p) {

        List<String> matchingLemmas = new LinkedList<String>();

        Document doc = cache.readFile(e.getSource());
        NodeList sentences = doc.getElementsByTagName("sentence");

        for (Integer index : e.getSentenceIndices()) {

            NodeList tokens = ((Element) sentences.item(index)).getElementsByTagName("token");
            for (int t = 0; t < tokens.getLength(); t++) {

                String pos = ((Element) tokens.item(t)).getElementsByTagName("POS").item(0).getTextContent();
                Matcher m = p.matcher(pos);
                if (m.find()) {

                    String lemma = ((Element) tokens.item(t)).getElementsByTagName("lemma").item(0).getTextContent();
                    matchingLemmas.add(lemma);

                }

            }

        }

        String[] matchingLemmaArray = new String[matchingLemmas.size()];
        return matchingLemmas.toArray(matchingLemmaArray);

    }

}
