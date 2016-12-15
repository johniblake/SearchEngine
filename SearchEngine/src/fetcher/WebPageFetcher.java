/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fetcher;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import webpage.URL;
import webpage.WebPage;

/**
 *
 * @author johnblake
 */
public class WebPageFetcher {
    private Document document;
    
    public WebPageFetcher(){
        
    }
    /**
     * Fetch http from url
     * @param webpage object containing url of page to be fetched
     * @return FetchResults object containing the parsed elements of the http document that has been fetched
     * @throws Exception 
     */
    public Document fetchPage(URL webURL) throws Exception{
        try {
            String url = webURL.getURL();
            document = Jsoup.connect(url)
            .data("query", "Java")
            .userAgent("User-Agent:Mozilla/5.0")
            .timeout(3000)
            .get();
            
        } catch (IOException ex) {
            Logger.getLogger(URL.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return document;
    }
}
