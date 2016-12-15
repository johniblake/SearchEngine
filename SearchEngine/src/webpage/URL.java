/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webpage;

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
public class URL {
    private String url;
    private int docID;
    private int parentDocID;
    private String parentURL;
    private short depth;
    private String domain;
    private String subDomain;
    private String path;
    private String anchorText;
    private byte priority;
    private String tag;

    public String getURL() {
        return url;
    }
    
    public void setURL(String url) {
        this.url = url;
    }

    public int getDocID() {
        return docID;
    }
    
    public void setDocid(int docid) {
        this.docID = docid;
    }

    public int getParentDocid() {
        return parentDocID;
    }
    
    public void setParentDocid(int parentDocid) {
        this.parentDocID = parentDocid;
    }

    public String getParentURL() {
        return parentURL;
    }
    
    public void setParentURL(String parentUrl) {
        this.parentURL = parentURL;
    }

    public short getDepth() {
        return depth;
    }
    
    public void setDepth(short depth) {
        this.depth = depth;
    }

    public String getDomain() {
        return domain;
    }
    
    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getSubDomain() {
        return subDomain;
    }
    
    public void setSubDomain(String subDomain) {
        this.subDomain = subDomain;
    }

    public String getPath() {
        return path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }

    public String getAnchorText() {
        return anchorText;
    }
    
    public void setAnchorText(String anchorText) {
        this.anchorText = anchorText;
    }

    public byte getPriority() {
        return priority;
    }
    
    public void setPriority(byte priority) {
        this.priority = priority;
    }

    public String getTag() {
        return tag;
    }
    
    public void setTag(String tag) {
        this.tag = tag;
    }
    
}
