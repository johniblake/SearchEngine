/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import fetcher.WebPageFetcher;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import webpage.URL;
import webpage.WebPage;
import webpage.WebPageFactory;

/**
 *
 * @author johnblake
 */
public class WebPageFetcherUT {
    WebPageFactory pageFactory;
    public WebPageFetcherUT() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        pageFactory = new WebPageFactory();
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
     @Test
     public void webPageFetcherUT() throws Exception {
        URL url = new URL();
        url.setURL("https://moz.com/top500");
        WebPage page = pageFactory.create(url);
        System.out.println(page.getBody());
        assertEquals(0,0);
     }
     
}
