package edu.columbia.cs.event.pipeline.component;

import edu.columbia.cs.event.pipeline.*;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.io.*;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 10/29/13
 * Time: 11:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class CoreNlpComponent implements PipelineComponent {

    private static Pattern extractPattern = Pattern.compile("<article id=\"([^\"]+)\".*<raw>(.*)<\\/raw>",Pattern.MULTILINE|Pattern.DOTALL);
    private StanfordCoreNLP pipeline;

    public void init() {
        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        props.put("parse.maxlen", "100");
        pipeline = new StanfordCoreNLP(props);



    }

    public void processTimeUnit(edu.columbia.cs.event.pipeline.TimeUnit timeUnit) {

        if (!timeUnit.getArticleMetaDirectory().exists())
            timeUnit.getArticleMetaDirectory().mkdirs();

        ExecutorService es = Executors.newFixedThreadPool(3);
        for (File xmlFile : timeUnit.getArticleDirectory().listFiles()) {
            try {
                Matcher m = extractPattern.matcher( IOUtils.slurpFile(xmlFile) );
                if( m.find() ) {
                    //System.out.println(m.group(1)+": "+ m.group(2) );
                    //System.out.println();
                    String fileId = m.group(1);
                    String fileText = m.group(2);


                    //fileText = fileText.replaceAll("([^\\.\n])\\n", "$1.\n");
                    //System.out.println(fileText);
                    File outputFile = new File(timeUnit.getArticleMetaDirectory(), fileId+".xml");
                    es.execute(new CoreNlpComponentThread(fileText, outputFile, pipeline));



                }
                m = null;
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

        try {
            es.shutdown();
            es.awaitTermination(Long.MAX_VALUE, java.util.concurrent.TimeUnit.HOURS);

        } catch (InterruptedException ie) {
            ie.printStackTrace();
            System.exit(-1);
        }
            /*

        for(final File articleFile : timeUnit.getArticleDirectory().listFiles()) {
            System.out.println("\t"+articleFile.getName());
            try {

                Matcher m = extractPattern.matcher( IOUtils.slurpFile(articleFile) );

                if( m.find() ) {
                    //System.out.println(m.group(1)+": "+ m.group(2) );
                    //System.out.println();
                    final String fileId = m.group(1);
                    final String fileText = m.group(2);

                    es.execute(new CoreNlpComponentThread(fileText));



                }

            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.exit(-1);
            }
        }

        Redwood.Util.threadAndRun("StanfordCoreNLP <" + 1 + " threads>", toRun, 1);
        System.out.println("Finished.");
              */

    }

    private class CoreNlpComponentThread implements Runnable {

        private String text;
        private StanfordCoreNLP pipeline;
        private File outputFile;

        public CoreNlpComponentThread(String text, File outputFile, StanfordCoreNLP pipeline) {
            this.text = text;
            this.pipeline = pipeline;
            this.outputFile = outputFile;
        }

        public void run() {

            Annotation annotation = new Annotation( text );
            SubEventPipeline.logTabbed("Processing fileid: " + outputFile.getName());
            this.pipeline.annotate(annotation);



            //File outputFilename = new File( timeUnit.getArticleMetaDirectory(), fileId+".xml");
            try {
                OutputStream fos = new BufferedOutputStream(new FileOutputStream(outputFile));
                Class clazz = Class.forName("edu.stanford.nlp.pipeline.XMLOutputter");
                Method method = clazz.getMethod("xmlPrint", Annotation.class, OutputStream.class, StanfordCoreNLP.class);
                method.invoke(null, annotation, fos, pipeline);
                fos.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.exit(-1);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(-1);
            }

        }




    }

}
