/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import indexer.DocIndex;
import indexer.InvertedIndex;
import indexer.LinkGraph;
import java.util.ArrayList;
import java.util.HashSet;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import webpage.WebPage;

/**
 *
 * @author johnblake
 */
public class InvertedIndexUT {
    
    public InvertedIndexUT() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
     public void emptyTest() throws Exception {
        InvertedIndex fi = new InvertedIndex();
        DocIndex di = new DocIndex();
        LinkGraph li = new LinkGraph();
        String document = "This is the text of the body!";
//        fi.processDocument(document, 1337);
//        fi.processDocument(document, 1458);
        String searchTerm = "top";
        HashSet<String> docIDs = fi.getDocIDsFromWord(searchTerm);
        System.out.println("WebPages containing the word '"+searchTerm+"':");
        for(String id:docIDs){
            String url = di.getURLByDocID(id);
            String pageRank = li.getPageRank(id);
            System.out.println("Page Rank: " + pageRank);
            System.out.println("Doc ID: " + id + " URL: " + url);
        }
        assertEquals(0,0);
     }
}
