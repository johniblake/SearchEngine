/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package indexer;

import redis.clients.jedis.Jedis;

/**
 * This class manages the document index database. The document index keeps track
 * of which urls have been seen by the crawlers, and assigns all new urls a unique ID.
 * @author johnblake
 */
public class DocIndex { 
    private final Jedis docIDsDB;
    private final URLResolver urlResolver;
    private int lastDocID;
    
    // anytime the database creates a new ID, this lock must be acquired to avoid
    // creating the same ID in two different threads.
    private static final Object idLock = new Object();
    
    /**
     * Empty constructor
     */
    public DocIndex(){
        lastDocID = 0;
        docIDsDB = new Jedis("localhost",6380);
        System.out.println("Connected to Document Index server successfully");
        System.out.println("Server is running:" + docIDsDB.ping());
        urlResolver = new URLResolver();
    }
    
    /**
     * Check if a docID exists for a given url; if it does not return -1.
     * @param url 
     * @return docID associated with a given url
     */
    public int getDocID(String url){
        synchronized(idLock){
            int docID = -1;
        String serverResponse;
        serverResponse = docIDsDB.get(url);
        try{
            if (!serverResponse.equals("null")){
                //System.out.println("docID exists!");
                docID = Integer.parseInt(serverResponse);
                return docID;
            }
        }catch(NullPointerException n){
            //System.out.println("docID does not exist");
        }
        return docID;
        }
    }
    
    /**
     * Create new (url, docID pair) and return the new docID
     * used in the case that a url has already been crawled.
     * @param url
     * @return 
     */
    public int getNewDocID(String url){
        synchronized(idLock){
            int docID;
            String response = docIDsDB.get(url);
            //System.out.println(response);
            docID = getDocID(url);
            if (docID == -1){
                ++lastDocID;
                return lastDocID;
            }
            return docID;
        }
    }
    
    /**
     * add entry to docID database if none exists. This method is to be used
     * on startup when adding the seed urls to the frontier queue.
     * @param url
     * @param docID 
     */
    public void addEntry(String url, int docID){
        int prevDocID = getDocID(url);
        if (prevDocID >= 0){
            if (prevDocID == docID){
                return;
            }
        }
        synchronized(idLock){
            if (docID < lastDocID){
                return;
            }
            docIDsDB.set(url, Integer.toString(docID));
            urlResolver.addEntry(url, docID);
            lastDocID = docID;
        }
    }
    /**
     * add entry to docID database if none exists.
     * @param url 
     */
    public void addEntry(String url){
        int docID = getNewDocID(url);
        synchronized(idLock){
            docIDsDB.set(url, Integer.toString(docID));
            urlResolver.addEntry(url, docID);
        }
    }
    
    public String getURLByDocID(String docID){
        return urlResolver.getURL(docID);
    }
    
    /**
     * checks if a url has been crawled
     * @param url
     * @return true if the url exists in the docID database.
     */
    public boolean isSeen(String url){
        return getDocID(url) != -1;
    }
    
    public void flush(){
        synchronized(idLock){
            docIDsDB.flushAll();
        }
    }
    
    public void close(){
        docIDsDB.close();
    }
}
