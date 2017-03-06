/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package indexer;

import java.util.ArrayList;
import java.util.HashSet;
import redis.clients.jedis.Jedis;

/**
 * This class manages the document index database. The document index keeps track
 * of which urls have been seen by the crawlers, and assigns all new urls a unique ID.
 * @author johnblake
 */
public class InvertedIndex { 
    private final Jedis invertedIndexDB;
    
    // anytime the database creates a new ID, this lock must be acquired to avoid
    // creating the same ID in two different threads.
    private static final Object idLock = new Object();
    private static ArrayList<String> STOP_WORDS= new ArrayList<String>();
    private static final String BAD_CHARS = "[^a-zA-Z]+";
    /**
     * Empty constructor
     */
    public InvertedIndex(){
        invertedIndexDB = new Jedis("localhost",6381);
        System.out.println("Connected to Inverted Index server successfully");
        System.out.println("Server is running:" + invertedIndexDB.ping());
        String[] stopWords= {"able", "about", "across", 
        "after", "all", "almost", "also", "am", "among", "an", "and", "any", "are",
        "as", "at", "be", "because", "been", "but", "by", "can", "cannot", "could", 
        "dear", "did", "do", "does", "either", "else", "ever", "every", "for", "from", 
        "get", "got", "had", "has", "have", "he", "her", "hers", "him", "his", "how", 
        "however", "if", "in", "into", "is", "it", "its", "just", "least", "let", 
        "like", "likely", "may", "me", "might", "most", "must", "my", "neither", "no", 
        "nor", "not", "of", "off", "often", "on", "only", "or", "other", "our", "own", 
        "rather", "said", "say", "says", "she", "should", "since", "so", "some", "than", 
        "that", "the", "their", "them", "then", "there", "these", "they", "this", "tis", 
        "to", "too", "twas", "us", "wants", "was", "we", "were", "what", "when", "where", 
        "which", "while", "who", "whom", "why", "will", "with", "would", "yet", "you", "your"};
        for (String word: stopWords){
            STOP_WORDS.add(word);
        }
 
    }
    

    
    /**
     * Check if a docID exists for a given url; if it does not return -1.
     * @param url 
     * @return docID associated with a given url
     */
    public HashSet<String> getDocIDsFromWord(String word){
        HashSet<String> docIDs = (HashSet<String>)invertedIndexDB.smembers(word);
        return docIDs;
    }
    
    
    /**
     * add entry to docID database if none exists. This method is to be used
     * on startup when adding the seed urls to the frontier queue.
     * @param url
     * @param docID 
     */
    public void addEntry(String word, int docID){
        synchronized(idLock){
            invertedIndexDB.sadd(word, Integer.toString(docID));
        }
    }
    
    public void processDocument(String document, int docID){
        document = document.replaceAll(BAD_CHARS, " ");
        String[] docWords = document.split(" ");
        for (String word: docWords){
            word = word.toLowerCase();
            if (word.length() >=2){
                if (!STOP_WORDS.contains(word)){
                    addEntry(word,docID);
                }
            }
        }
    }
    
    
    public void flush(){
        synchronized(idLock){
            invertedIndexDB.flushAll();
        }
    }
    
    public void close(){
        invertedIndexDB.close();
    }
}
