/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package indexer;

import webpage.URL;
import webpage.WebPage;

/**
 * Enqueues webpages from crawlers to be stored in the forward index
 * Stores webpages in the format (syncBits,lengthBits,compressedPacket)
 * Sends documents to docIDServer for further processing
 * where Compressed packet is in the format (docID,ecode,URLLength,URL,pageHTML)
 * @author johnblake
 */
public class StoreServer {
    public StoreServer(){
        
    }
    
    /**
     * Add a new (docID, htmldocument) pair to forward index
     * @param webpage 
     */
    public void addDocumentToDB(WebPage webpage){
        
    }
    /**
     * Add a new (docID, anchor text) pair to forward index
     * @param url
     */
    public void addAnchorToDB(URL url){
        
    }
}
