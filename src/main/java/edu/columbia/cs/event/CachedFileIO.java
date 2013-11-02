package edu.columbia.cs.event;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 10/26/13
 * Time: 6:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class CachedFileIO {

    private DocumentBuilderFactory dbFactory;
    private DocumentBuilder dBuilder;

    private int cacheSize = 1000;
    private List<String> filenames = new LinkedList<String>();
    private List<Document> documents = new LinkedList<Document>();

    public static CachedFileIO CACHED_FILE_IO = new CachedFileIO();

    private CachedFileIO() {

        try {
            dbFactory = DocumentBuilderFactory.newInstance();
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
            System.exit(-1);
        }

    }

    public static CachedFileIO getCacheFileIO() {
        return CACHED_FILE_IO;
    }

    public void setCacheSize(int numDocs) {
        cacheSize = numDocs;
        filenames = filenames.subList(0,numDocs);
        documents = documents.subList(0, numDocs);
    }


    public Document readFile(File xmlFile) {

        int index = filenames.indexOf(xmlFile.toString());

        if (index > 0) {

            Document doc = documents.get(index);
            updateIndex(index);
            return doc;

        } else {

            Document doc = null;

            try {

                doc = dBuilder.parse(xmlFile);

                cache(doc, xmlFile);


            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.exit(-1);
            } catch (SAXException saxe) {
                saxe.printStackTrace();
                System.exit(-1);
            }


            return doc;
        }

    }

    private void cache(Document doc, File xmlFile) {

        documents.add(0, doc);
        filenames.add(0, xmlFile.toString());

        if (documents.size() > cacheSize) {

            documents.remove(cacheSize-1);
            filenames.remove(cacheSize-1);

        }

    }

    private void updateIndex(int index) {


        if (index > 0 && index < cacheSize) {
            String swapStr = filenames.remove(index);
            Document swapDoc = documents.remove(index);

            filenames.add(0, swapStr);
            documents.add(0, swapDoc);

        }

    }

}


