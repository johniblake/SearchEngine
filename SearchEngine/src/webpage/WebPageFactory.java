/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webpage;

import fetcher.WebPageFetcher;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * A Factory class containing the method create() which builds and returns
 * a webpage given a URL
 * @author johnblake
 */
public class WebPageFactory {
    /**
     * The static class that build WebPage objects given a URL.
     * @param url url of the page to be downloaded and parsed
     * @return webPage the newly created WebPage object.
     * @throws Exception 
     */
    public static WebPage create(URL url) throws Exception{
        Response response = WebPageFetcher.fetchPage(url);
        int statusCode = response.statusCode();
        if (statusCode < 200 || statusCode > 299){
            return null;
        }
        WebPage webPage = new WebPage();
        webPage.setUrl(url);
        String contentType = response.contentType(); 
        Document webDocument = response.parse();
        String title = webDocument.title();
        Elements links = webDocument.select("a[href]");
        Elements filteredLinks = new Elements();
        Element body = webDocument.body();
        webPage.setTitle(title);
        webPage.setLinks(links);
        webPage.setBody(body.text());
        webPage.setContentType(contentType);
        
        return webPage;
    }
}
