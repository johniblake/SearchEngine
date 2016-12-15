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
import indexer.StoreServer;
import frontier.URLServer;
import indexer.DocIDServer;
import indexer.LinkServer;


/**
 * This class contains all methods and fields necessary for creating crawler threads and scheduling jobs (i.e.serving URLs)
 * It also schedules the saving of webpage data to the repository
 * @author johnblake
 */

public class WebCrawlerController {
    protected WebPageFetcher fetcher;
    protected boolean finished;
    protected boolean shuttingDown;
    protected URLServer urlServer;
    protected StoreServer storeServer;
    protected DocIDServer docIDServer;
    protected LinkServer linkServer;

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
        this.storeServer = new StoreServer();
        this.urlServer = new URLServer();
        this.docIDServer = new DocIDServer();
        this.linkServer = new LinkServer();
    }
    
    public StoreServer getStoreServer(){
        return this.storeServer;
    }
    
    public URLServer getURLServer(){
        return this.urlServer;
    }
    
    public DocIDServer getDocIDServer(){
        return this.docIDServer;
    }
    
    public LinkServer getLinkServer(){
        return this.linkServer;
    }

    /**
     * generate crawler threads and start them
     * @param numCrawlers number of crawlers to deploy
     * @throws IOException 
     */
    public void start(int numCrawlers) throws IOException{
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
            final WebCrawlerController server = this;
            Thread monitorThread = new Thread(new Runnable() {
                
                @Override
                public void run() {
                    
                    for (int j = 0; j < threads.size(); j++){
                        Thread thread = threads.get(j);
                        try {
                            thread.wait(100);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(WebCrawlerController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                    }
                }
            });
        } catch(Exception e){
            System.err.println("URLServer error");
        }
    }
    
    /**
     * add seed url to the docID database and the frontier
     * @param url
     * @param docID 
     */
    public void addSeed(String url, int docID){
        
    }
    
     /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
//        int numThreads = 8;
//        WebCrawlerController controller = new WebCrawlerController();
//        controller.start(8);
    }
}
