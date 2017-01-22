/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import indexer.LinkGraph;
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
public class LinkGraphUT {
    LinkGraph linkGraph;
    
    public LinkGraphUT() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        linkGraph = new LinkGraph();
    }
    
    @After
    public void tearDown() {
        linkGraph.shutDown();
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
     @Test
     public void LinkGraphTest() {
         linkGraph.addLink(0, 1337);
         assertEquals(0,0);
     }
}
