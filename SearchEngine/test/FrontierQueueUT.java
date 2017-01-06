/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import frontier.FrontierQueue;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import webpage.URL;

/**
 *
 * @author johnblake
 */
public class FrontierQueueUT {
    FrontierQueue fq;
    public FrontierQueueUT() {
        
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        fq = new FrontierQueue();
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testAdd(){
        URL url1 = new URL();
        url1.setURL("www.google.com");
        url1.setPriority((double)10);
        URL url2 = new URL();
        url2.setURL("www.facebook.com");
        url2.setPriority((double)9);
        
        fq.addURL(url1);
        fq.addURL(url2);
        
        //useless test, basically testing that no errors are thrown in the
        //process of adding new urls to the priority queue.
        assertEquals(0,0);
    }
    
    @Test
    public void testGetNext(){
        URL url1 = fq.getNext();
        URL url2 = fq.getNext();
        
        String expectedFirst = "www.facebook.com";
        String expectedSecond = "www.google.com";
        
        assertEquals(expectedFirst,url1.getURL());
        assertEquals(expectedSecond,url2.getURL());
        
        
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
