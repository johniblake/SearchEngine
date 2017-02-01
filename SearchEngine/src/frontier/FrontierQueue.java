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
    private final Jedis queue;
    private static final Object frontierLock = new Object();
    private static long queueLength;
    private static boolean isFinished;
    
    /**
     * empty constructor
     */
    public FrontierQueue(){
        queue = new Jedis("localhost",6379);
        System.out.println("Connected to Frontier Queue server successfully");
        System.out.println("Server is running:" + queue.ping());
        this.queueLength = 0;
        isFinished = false;
    }
    
    /**
     * add url to frontier queue
     * @param url 
     */
    public void addURL(URL url){
        String link = url.getURL();
        double score = url.getPriority();
        synchronized(frontierLock){
            queue.zadd("frontier", score, link);
            queueLength++;
        }
    }
    
    /**
     * Get next url in queue
     * @return 
     */
    public URL getNext(){
        URL url = new URL();
        Set<String> links;
        synchronized(frontierLock){
            links = queue.zrange("frontier", 0, 0);
            queue.zremrangeByRank("frontier", 0, 0);
            queueLength--;
        }
        for (String link:links){
            System.out.println("Got link: " + link);
            url.setURL(link);
        }
        return url;
    }
    
    public long getQueueLength(){
        return queueLength;
    }
    
    public boolean isFinished(){
        return isFinished;
    }
        
    public void finish(){
        synchronized(frontierLock){
            isFinished = true;
        }
    }
    
    public void close(){
        queue.close();
    }
}
