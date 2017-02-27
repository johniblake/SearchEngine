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
    private static int queueCycle;
    private static String keyName;
    
    /**
     * empty constructor
     */
    public FrontierQueue(){
        queue = new Jedis("localhost",6379);
        System.out.println("Connected to Frontier Queue server successfully");
        System.out.println("Server is running:" + queue.ping());
        this.queueLength = 0;
        isFinished = false;
        queueCycle = 0;
        String keyName = "";
    }
    
    /**
     * add url to frontier queue
     * @param url 
     */
    public void addURL(URL url){
        System.out.println("attempting to add url to frontier.");
        String link = url.getURL();
        double score = url.getPriority();
        synchronized(frontierLock){
            int cycle = modCycle(queueCycle);
            switch(cycle){
                case 0: keyName = "frontier0";
                        break;
                case 1: keyName = "frontier1";
                        break;
                case 2: keyName = "frontier2";
                        break;
                case 3: keyName = "frontier3";
                        break;
                case 4: keyName = "frontier4";
                        break;
                case 5: keyName = "frontier5";
                        break;
                case 6: keyName = "frontier6";
                        break;
                case 7: keyName = "frontier7";
                        break;
                case 8: keyName = "frontier8";
                        break;
                case 9: keyName = "frontier9";
                        break;
                case 10: keyName = "frontier10";
                        break;
                case 11: keyName = "frontier11";
                        break;
                case 12: keyName = "frontier12";
                        break;
                case 13: keyName = "frontier13";
                        break;
                case 14: keyName = "frontier14";
                        break;
                case 15: keyName = "frontier15";
                        break;
                default: keyName = "frontier";
                        break;
            }
            queue.zadd(keyName, score, link);
            System.out.println("added url to frontier.");
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
            links = queue.zrange(keyName, 0, 0);
            queue.zremrangeByRank(keyName, 0, 0);
            queueLength--;
            cycle();
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
    
    public void cycle(){
        queueCycle = (queueCycle + 1) % 150;
    }
    
    public int modCycle(int cycle){
        cycle = (int)((long)(cycle/10));
        return cycle;
    }
}
