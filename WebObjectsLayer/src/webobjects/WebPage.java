/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webobjects;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author johnblake
 */
public class WebPage {
    private Anchor anchor;
    private String webPageHash;
    private int anchorParseStatus;
    private int emailParseStatus;
    private Document document;
    private Elements links;

    public String getWebPageHash() {
        return webPageHash;
    }
    
    public Anchor getAnchor(){
        return anchor;
    }

    public int getAnchorParseStatus() {
        return anchorParseStatus;
    }

    public int getEmailParseStatus() {
        return emailParseStatus;
    }

    public Document getDocument() {
        return document;
    }
    
    public Elements getLinks() {
        return links;
    }

    
    
    public WebPage(Anchor anchor) throws Exception {
        this.anchor = anchor;
        this.webPageHash = Hasher.toSHA256(anchor.getAnchorHash() + Common.getTimestamp().toString());
        this.anchorParseStatus = 0;
        this.emailParseStatus = 0;
    }
    
    public void getDocumentFromWeb(){
        try {
            document = Jsoup.connect(anchor.getAnchorUrl())
            .data("query", "Java")
            .userAgent("User-Agent:Mozilla/5.0")
            .timeout(3000)
            .get();
            
            links = document.select("a");
//            for (Element e:links){
//                System.out.println(e.attr("abs:href"));
//                
//            }
        } catch (IOException ex) {
            Logger.getLogger(WebPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
