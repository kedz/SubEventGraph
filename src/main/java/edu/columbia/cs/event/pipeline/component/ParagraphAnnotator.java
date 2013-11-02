package edu.columbia.cs.event.pipeline.component;

import edu.columbia.cs.event.Slurp;
import edu.columbia.cs.event.pipeline.SubEventPipeline;
import edu.columbia.cs.event.pipeline.TimeUnit;
import edu.columbia.cs.event.annotations.Excerpt;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 11/1/13
 * Time: 6:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class ParagraphAnnotator implements PipelineComponent {

    private StanfordCoreNLP pipeline;

    public void init() {

        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit");
        pipeline = new StanfordCoreNLP(props);


    }

    public void processTimeUnit(TimeUnit timeUnit) {

        File articleDir = timeUnit.getArticleDirectory();
        File articleMetaDir = timeUnit.getArticleMetaDirectory();

        ConcurrentLinkedQueue<Excerpt> excerpts = new ConcurrentLinkedQueue<Excerpt>();

        ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*4);

        for(File articleFile : articleDir.listFiles()) {
            File metaXmlFile = new File(articleMetaDir, articleFile.getName());

            if (metaXmlFile.exists())
                es.execute(new graffAnnotatorThread(articleFile, metaXmlFile, excerpts));
        }

        try {
            es.shutdown();
            es.awaitTermination(Long.MAX_VALUE, java.util.concurrent.TimeUnit.HOURS);

        } catch (InterruptedException ie) {
            ie.printStackTrace();
            System.exit(-1);
        }
        System.out.println("Finished.");

        timeUnit.getExcerpts().addAll(excerpts);

    }


    private class graffAnnotatorThread implements Runnable {

        private File articleXml;
        private File coreNlpXmlFile;
        private ConcurrentLinkedQueue<Excerpt> outputQueue;

        public graffAnnotatorThread(File articleXml, File coreNlpXmlFile, ConcurrentLinkedQueue<Excerpt> outputQueue) {
            this.articleXml = articleXml;
            this.coreNlpXmlFile = coreNlpXmlFile;
            this.outputQueue = outputQueue;
        }

        public void run() {

            SubEventPipeline.logTabbed(String.format("Annotating %s", articleXml));

            List<List<CoreMap>> graffAnnotations = new LinkedList<List<CoreMap>>();
            String[] graffs = Slurp.rawParagraphsFromArticleXml(articleXml);

            // Split each paragraph into sentences
            for (String graff : graffs) {

                Annotation graffSentences = new Annotation(graff);

                pipeline.annotate(graffSentences);
                List<CoreMap> sentences = graffSentences.get(CoreAnnotations.SentencesAnnotation.class);
                graffAnnotations.add(sentences);

            }


            // Create Excerpt objects for all paragraphs
            int graffIndex = 0;
            int sentenceIndex = 0;

            for (List<CoreMap> sentences : graffAnnotations) {

                if (sentences.size()>0) {

                    String label = String.format("%s.p%d", articleXml.getName().replaceAll(".xml", ""), graffIndex);
                    List<Integer> sentenceIndices = new LinkedList<Integer>();
                    for (CoreMap sentence : sentences)
                        sentenceIndices.add(sentenceIndex++);

                    Excerpt paragraph = new Excerpt(coreNlpXmlFile, sentenceIndices);
                    paragraph.setLabel(label);
                    outputQueue.add(paragraph);

                    graffIndex++;
                }
            }

        }

    }

}
