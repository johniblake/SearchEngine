/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import webcrawler.WebCrawlerController;

/**
 *
 * @author johnblake
 */
public class WebCrawlerControllerUT {
    
    public WebCrawlerControllerUT() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() throws IOException {
        WebCrawlerController crawlerController = new WebCrawlerController();
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
     @Test
     public void testCrawler() throws InterruptedException, IOException {
        int numThreads = 4;
        WebCrawlerController controller = new WebCrawlerController();
        controller.addSeed("https://moz.com/top500", -1);
        controller.start(4);
        Thread.sleep(30000);
        controller.shutdown();
     }
}
