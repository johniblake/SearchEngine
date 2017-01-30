/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import indexer.ForwardIndex;
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
public class ForwardIndexUT {
    
    public ForwardIndexUT() {
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

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
     @Test
     public void emptyTest() {
        ForwardIndex fi = new ForwardIndex();
        WebPage webpage = new WebPage();
        webpage.setBody("This is the text of the body!");
        fi.addDocumentToDB(webpage, 94);
        assertEquals(0,0);
     }
}
