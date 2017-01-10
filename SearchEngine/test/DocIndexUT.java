/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import indexer.DocIndex;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author johnblake
 */
public class DocIndexUT {
    DocIndex docIndex;
    public DocIndexUT() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        docIndex = new DocIndex();
    }
    
    @After
    public void tearDown() {
        docIndex.flush();
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void addEntryTest() {
        String url = "www.facebook.com";
        String url2 = "www.google.com";
        int id = 0;
        docIndex.addEntry(url,id);
        docIndex.addEntry(url2);
        docIndex.addEntry(url2);
        int returnID1 = docIndex.getDocID(url);
        int returnID2 = docIndex.getDocID(url2);
        
        //test whether or not entries were added and whether or not isSeen method works.
        assertEquals(0,returnID1);
        assertEquals(1,returnID2);
        assertEquals(true, docIndex.isSeen(url));
        assertEquals(true, docIndex.isSeen(url2));
        assertEquals(false, docIndex.isSeen("hello"));
    }
}
