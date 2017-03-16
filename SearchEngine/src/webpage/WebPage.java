/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webpage;

import java.util.Map;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Contains the data for a fetched and parsed webpage. 
 * @author johnblake
 */
public class WebPage {
    
    /**
     * The URL of this page.
     */
    protected URL url;

    /**
     * Status of the page
     */
//    protected int statusCode;

    /**
     * The ContentType of this page.
     * For example: "text/html; charset=UTF-8"
     */
    protected String contentType;

    /**
     * Headers which were present in the response of the fetch request
     */
    protected Map<String,String> responseHeaders;

    /**
     * The page body
     */
    protected String body;
    
    
    /**
     * The page title
     */
    protected String title;
    
    
    /**
     * URLs found on this webpage
     */
    protected Elements links;
    

    public WebPage() {
  
    }
    
    public String getTitle(){
        return title;
    }
    

    public URL getUrl() {
        return url;
    }

//    public int getStatusCode() {
//        return statusCode;
//    }

    public String getContentType() {
        return contentType;
    }

    public Map<String, String> getResponseHeaders() {
        return responseHeaders;
    }

    public String getBody() {
        return body;
    }

    public Elements getLinks() {
        return links;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

//    public void setStatusCode(int statusCode) {
//        this.statusCode = statusCode;
//    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setResponseHeaders(Map<String, String> responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setLinks(Elements links) {
        this.links = links;
    }

}
