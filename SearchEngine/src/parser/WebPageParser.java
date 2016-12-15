/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import org.jsoup.nodes.Document;
import webpage.URL;
import webpage.WebPage;

/**
 * Parses fetched html stored in the repository in order to obtain the page's header and body fields 
 * @author johnblake
 */
public class WebPageParser {
    
    /**
     * parse data from fetched document and use data set all fields
     * @param webPage
     * @param document
     * @throws Exception 
     */
    public static WebPage loadFields(WebPage webPage, Document document) throws Exception {
        //parse data from document and use data set all fields
        return webPage;
    }
    
    public static WebPage parseWebPage(URL url, Document document) throws Exception{
        WebPage results = loadFields(new WebPage(url), document);
        return results;
    }
}
