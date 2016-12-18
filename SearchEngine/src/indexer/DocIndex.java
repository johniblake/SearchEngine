/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package indexer;

/**
 * This class manages the docID database (document index)
 * @author johnblake
 */
public class DocIndex {
    //private final Database docIDsDB;
    private int lastDocID;
    
    /**
     * Empty constructor
     */
    public DocIndex(){
        lastDocID = 0;
    }
    
    /**
     * Check if a docID exists for a given url; if it does not return -1.
     * @param url 
     * @return docID associated with a given url
     */
    public int getDocID(String url){
        int docID = -1;
        return docID;
    }
    
    /**
     * Create new (url, docID pair) and return the new docID
     * used in the case that a url has already been crawled.
     * @param url
     * @return 
     */
    public int getNewDocID(String url){
        return 0;
    }
    
    /**
     * add entry to docID database if none exists
     * @param url
     * @param docID 
     */
    private void addEntry(String url, int docID){
        
    }
    
    /**
     * checks if a url has been crawled
     * @param url
     * @return true if the url exists in the docID database.
     */
    public boolean isSeen(String url){
        return getDocID(url) != -1;
    }
    
}
