/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontier;

import java.util.Set;
import webpage.URL;
import redis.clients.jedis.Jedis;

/**
 * Manages the priority queue of URLs to be crawled
 * @author johnblake
 */
public class FrontierQueue {
    Jedis queue;
    
    /**
     * empty constructor
     */
    public FrontierQueue(){
        queue = new Jedis("localhost");
        System.out.println("Connected to server successfully");
        System.out.println("Server is running:" + queue.ping());
    }
    
    /**
     * add url to frontier queue
     * @param url 
     */
    public void addURL(URL url){
        String link = url.getURL();
        double score = url.getPriority();
        queue.zadd("frontier", score, link);
    }
    
    /**
     * Get next url in queue
     * @return 
     */
    public URL getNext(){
        URL url = new URL();
        Set<String> links = queue.zrange("frontier", 0, 0);
        queue.zremrangeByRank("frontier", 0, 0);
        for (String link:links){
            System.out.println(link);
            url.setURL(link);
        }
        //do I need docID at this point?
        url.setDocid(0);
        return url;
    }
}
