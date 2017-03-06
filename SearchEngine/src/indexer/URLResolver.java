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
public class URLResolver { 
    private final Jedis URLsDB;
    private int lastDocID;
    
    // anytime the database creates a new ID, this lock must be acquired to avoid
    // creating the same ID in two different threads.
    private static final Object URL_LOCK = new Object();
    
    /**
     * Empty constructor
     */
    public URLResolver(){
        lastDocID = 0;
        URLsDB = new Jedis("localhost",6382);
        System.out.println("Connected to URL Resolver server successfully");
        System.out.println("Server is running:" + URLsDB.ping());
    }
    
    /**
     * Check if a URL exists for a given docID; if it does not return -1.
     * @param url 
     * @return docID associated with a given url
     */
    public String getURL(String docID){ 
        String serverResponse;
        serverResponse = URLsDB.get(docID);
        try{
            if (!serverResponse.equals("null")){
                //System.out.println("docID exists!");
                return serverResponse;
            }
        }catch(NullPointerException n){
            System.err.println("URL could not be resolved");
        }
        return serverResponse;
    }
    
    /**
     * add entry to docID database if none exists. This method is to be used
     * on startup when adding the seed urls to the frontier queue.
     * @param url
     * @param docID 
     */
    public void addEntry(String url, int docID){
        synchronized(URL_LOCK){
            URLsDB.set(Integer.toString(docID), url);
        }
    }
    
    public void flush(){
        synchronized(URL_LOCK){
            URLsDB.flushAll();
        }
    }
    
    public void close(){
        URLsDB.close();
    }
}
