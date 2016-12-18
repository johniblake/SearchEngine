/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fetcher;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import webpage.URL;

/**
 * Downloads a page from the web for a given url.
 * @author johnblake
 */
public class WebPageFetcher {
    /**
     * Fetch http from url
     * @param webpage object containing url of page to be fetched
     * @return FetchResults object containing the parsed elements of the http document that has been fetched
     * @throws Exception 
     */
    public static Response fetchPage(URL webURL) throws Exception{
        Connection.Response response = null;
        try {
            String url = webURL.getURL();
            response = Jsoup.connect(url)
            .data("query", "Java")
            .userAgent("User-Agent:Mozilla/5.0")
            .timeout(3000)
            .execute();
            
        } catch (IOException ex) {
            Logger.getLogger(URL.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return response;
    }
}
