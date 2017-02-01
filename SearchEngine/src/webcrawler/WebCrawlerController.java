/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import fetcher.WebPageFetcher;
import indexer.ForwardIndex;
import frontier.FrontierQueue;
import indexer.DocIndex;
import indexer.LinkGraph;
import webpage.URL;


/**
 * This class contains all methods and fields necessary for creating crawler threads and scheduling jobs (i.e.serving URLs)
 * It also schedules the saving of webpage data to the repository
 * @author johnblake
 */

public class WebCrawlerController {
    protected WebPageFetcher fetcher;
    protected boolean finished;
    protected boolean shuttingDown;
    protected FrontierQueue frontier;
    protected ForwardIndex forwardIndex;
    protected DocIndex docIndex;
    protected LinkGraph linkGraph;
    protected static final Object waitingLock = new Object();

    /**
     * Creates a crawling session and provides crawler threads a queue of urls
     * to crawl. It provides the crawlers a means for storing web data to 
     * the forward index and checking newly discovered links for existence in the 
     * docID database (document index).
     * @throws IOException 
     */
    public WebCrawlerController() throws IOException{
        this.finished = false;
        this.shuttingDown = false;
        this.forwardIndex = new ForwardIndex();
        this.frontier = new FrontierQueue();
        this.docIndex = new DocIndex();
        this.linkGraph = new LinkGraph();
        this.fetcher = new WebPageFetcher();
        
    }
    
    public ForwardIndex getForwardIndex(){
        return this.forwardIndex;
    }
    
    public FrontierQueue getFrontierQueue(){
        return this.frontier;
    }
    
    public DocIndex getDocIndex(){
        return this.docIndex;
    }
    
    public LinkGraph getLinkGraph(){
        return this.linkGraph;
    }

    /**
     * generate crawler threads and start them
     * @param numCrawlers number of crawlers to deploy
     * @throws IOException 
     */
    public void start(int numCrawlers) throws IOException{
        System.out.println("Starting crawl...");
        try{
            finished = false;
            final List<Thread> threads = new ArrayList<>();
            final List<WebCrawler> crawlers = new ArrayList<>();

            for (int i = 1; i <= numCrawlers; i++) {
                WebCrawler crawler = new WebCrawler();
                Thread thread = new Thread(crawler, "Crawler " + i);
                crawler.setThread(thread);
                crawler.init(i, this);
                thread.start();
                crawlers.add(crawler);
                threads.add(thread);
                System.out.println("Crawler "+ i + " started");
            }
            final WebCrawlerController crawlController = this;
            Thread monitorThread = new Thread(new Runnable() {
                
                @Override
                public void run() {
                    try {
                        //lock is acquired for monitor thread
                        synchronized (waitingLock) {
                            while (true) {
                                Thread.sleep(20000);
                                boolean someThreadWorking = false;
                                for (int j = 0; j < threads.size(); j++){
                                    Thread thread = threads.get(j);
                                    if (!thread.isAlive()){
                                        if (!shuttingDown){
                                            WebCrawler crawler = new WebCrawler();
                                            thread = new Thread(crawler, "Crawler " + (j + 1));
                                            threads.remove(j);
                                            threads.add(j, thread);
                                            crawler.setThread(thread);
                                            crawler.init(j + 1, crawlController);
                                            thread.start();
                                            crawlers.remove(j);
                                            crawlers.add(j, crawler);
                                            System.out.println("Crawler "+ j + " restarted");
                                        }
                                    } else if (!crawlers.get(j).isWaitingForNewURLs) {
                                        someThreadWorking = true;
                                    }

                                }
                                boolean shutOnEmpty = true;
                                if (!someThreadWorking && shutOnEmpty) {
                                    //wait 10 seconds to make sure no threads are alive
                                    Thread.sleep(10000);
                                    someThreadWorking = false;
                                    for (int k = 0; k < threads.size(); k++){
                                        Thread thread = threads.get(k);
                                        if (thread.isAlive() && !crawlers.get(k).isWaitingForNewURLs) {
                                            someThreadWorking = true;
                                        }
                                    }
                                    if (!someThreadWorking) {
                                        long queueLength = frontier.getQueueLength();
                                        if (queueLength > 0){
                                            continue;
                                        }
                                        System.out.println("No thread is working and no more URLs are in the queue.");
                                        Thread.sleep(5000);
                                        queueLength = frontier.getQueueLength();
                                        if (queueLength > 0){
                                            continue;
                                        }
                                        System.out.println("All crawlers have stopped Finishing the process...");
                                        frontier.finish();
                                        Thread.sleep(5000);
                                        frontier.close();
                                        docIndex.close();
                                        forwardIndex.closeConnection();
                                        finished = true;
                                        waitingLock.notifyAll();
                                        return;
                                    }
                                }
                            }
                        }
                    }catch (Exception e){
                        System.err.println("Error while monitoring threads.");
                    }
                    
                }
            });
            monitorThread.start();
            waitUntilFinish();
            if (monitorThread.isAlive()){
                monitorThread.stop();
            }
            
        } catch(Exception e){
            System.err.println("URLServer error");
        }
    }
    
    /**
     * Wait until this crawling session finishes.
     */
    public void waitUntilFinish() {
        while (!finished) {
            synchronized (waitingLock) {
                if (finished) {
                    return;
                }
                try {
                    waitingLock.wait();
                } catch (InterruptedException e) {
                    System.err.println("Error: "+ e);
                }
            }
        }
    }
    
    /**
     * add seed url to the docID database and the frontier
     * @param url
     * @param docID 
     */
    public void addSeed(String url, int docID){
        if (docID < 0){
            docID = docIndex.getDocID(url);
            if (docID > 0){
                System.out.println("This URL has already been seen");
                return;
            }
            docID = docIndex.getNewDocID(url);
        } else {
            try{
                docIndex.addEntry(url, docID);
            } catch (Exception e){
                System.err.println("Could not add seed: " + url);
            }
        }
        URL webUrl = new URL();
        webUrl.setURL(url);
        webUrl.setDocid(docID);
        webUrl.setDepth((short)0);
        frontier.addURL(webUrl);
        
    }
    
    public boolean isFinished(){
        return this.finished;
    }
    
    public boolean isShuttingDown() {
        return shuttingDown;
    }
    
    /**
     * Set the current crawling session set to 'shutdown'. Crawler threads
     * monitor the shutdown flag and when it is set to true, they will no longer
     * process new pages.
     */
    public void shutdown() {
        System.out.println("Shutting down...");
        this.shuttingDown = true;
        frontier.finish();
    }
    
     /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        int numThreads = 2;
        WebCrawlerController controller = new WebCrawlerController();
        controller.addSeed("https://moz.com/top500", -1);
        
        controller.start(2);
        Thread.sleep(10000);
        controller.shutdown();
    }   
}
