/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package indexer;

/**
 * Builds a graph of (url, parent set, child set) tuples to be iterated over in order to calculate
 * PageRank
 * @author johnblake
 */
public class LinkGraph {
    
    //Database database;
    
    /**
     * empty constructor
     */
    public LinkGraph(){
        //create new database
    }
    
    /**
     * add edge to graph if it does not already exist
     * @param url
     * @param parentURL 
     */
    public void addLink(String url, String childUrl){
        
    }
    
    /**
     * recurse over database using BFS, calculating and storing PageRank for each
     * child URL
     */
    public void recurse(String startURL){
        
    }
    
    /**
     * gets the pagerank of the url from the Links database
     * @param url
     * @return 
     */
    public int getPageRank(String url){
        int pagerank = 0;
        return pagerank;
    }
    
}
