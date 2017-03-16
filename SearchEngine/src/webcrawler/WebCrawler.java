
package webcrawler;

import webpage.URL;
import fetcher.WebPageFetcher;
import indexer.ForwardIndex;
import frontier.FrontierQueue;
import indexer.DocIndex;
import indexer.InvertedIndex;
import indexer.LinkGraph;
import indexer.TitleIndex;
import java.util.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import webpage.WebPage;
import webpage.WebPageFactory;

/**
 * Each instance of this class is a web client that downloads web pages and stores them in the WebPageRepository.
 * @author johnblake
 */
public class WebCrawler implements Runnable {
    protected int myId;
    protected String domainName;
    protected String queueFilePath;
    protected String crawledFilePath;
    protected String baseURL;
    protected String domainURL;
    protected ArrayList<String> queue;
    protected HashSet<String> crawled;
    protected Thread thread;
    protected WebCrawlerController crawlerController;
    protected WebPageFetcher fetcher;
    protected ForwardIndex forwardIndex;
    protected InvertedIndex invertedIndex;
    protected DocIndex docIndex;
    private FrontierQueue frontier;
    protected LinkGraph linkGraph;
    protected TitleIndex titleIndex;
    protected boolean isWaitingForNewURLs;
    
    public void init(int id, WebCrawlerController crawlerController) throws IOException {
        this.myId = id;
        this.crawlerController = crawlerController;
        this.fetcher = new WebPageFetcher();
        this.forwardIndex = crawlerController.getForwardIndex();
        this.invertedIndex = crawlerController.getInvertedIndex();
        this.frontier = crawlerController.getFrontierQueue();
        this.linkGraph = crawlerController.getLinkGraph();
        this.docIndex = crawlerController.getDocIndex();
        this.titleIndex = crawlerController.getTitleIndex();
        this.isWaitingForNewURLs = false;
        
    }
    public void setThread(Thread thread){
        this.thread = thread;
    }
    
    /**
     * Download webpage and use the ForwardIndex to store its contents to the repository
 use DocIDServer to check for the child links' existence in the docID DB
 and add them to the frontier if they have not been seen.
     * @param crawlerName
     * @param pageURL
     * @throws IOException
     * @throws Exception 
     */
    public void crawlPage(String crawlerName, URL pageURL) throws IOException, Exception{
        WebPage webPage = WebPageFactory.create(pageURL);
        String parentURL = pageURL.getURL();
        int parentID = docIndex.getDocID(parentURL);
        if (parentID == -1){
            parentID = docIndex.getNewDocID(parentURL);
            docIndex.addEntry(parentURL, parentID);
        }
        
        //send all URLs from webPage to docIDServer to add them to the anchors index/further process
        Elements relativeLinks = webPage.getLinks();
        TreeSet<String> links = new TreeSet();
        for (Element e:relativeLinks){
//            System.out.println("Found Link: " + e.attr("abs:href"));
            links.add(e.attr("abs:href"));
        }   
        while (links.size() > 0){
//            System.out.println("~~~~~~~~~~~~~~~~~~~~~~GOT HERE!~~~~~~~~~~~~~~~~~~~~~~~~~");
            String curURL = links.pollFirst();
            URL childURL = new URL();
            childURL.setURL(curURL);
            //set priority - random for now.
            childURL.setPriority(new Random().nextInt(1000));
            if (docIndex.isSeen(curURL)){
                // get url's id
                //System.err.println("Have seen " + curURL);
                int childID = docIndex.getDocID(curURL);
                addLinkToGraph(parentID, childID);
                
                
            }else{
                docIndex.addEntry(curURL);
                int childID = docIndex.getDocID(curURL);
                childURL.setParentDocid(parentID);
                childURL.setDocid(childID);
                //add to link graph with updated parent info
                addLinkToGraph(parentID, childID);
                //add child url to frontier
//                System.out.println("Calling frontier.addURL()");
                frontier.addURL(childURL);
            }
        }
        System.out.println("Adding page to inverted and forward indexes!");
        invertedIndex.processDocument(webPage.getBody(), parentID);
        addPageToRepository(webPage, parentID);
        titleIndex.addEntry(webPage.getTitle(), parentID);
    }
    
    /**
     * Use ForwardIndex to save webpage to the repository as a (docID,document) pair 
     * @param links 
     */
    public void addPageToRepository(WebPage page, int docID){
        
        forwardIndex.addDocumentToDB(page, docID);
    }
    
    /**
     * Use DocIDServer and URL Server to add link to frontier queue and the 
     * docID DB
     * @param url 
     */
    public void addURLToFrontier(URL url){
        frontier.addURL(url);
    }
    
    /**
     * Use LinkServer to add this edge to the graph of the web
     * @param curURL
     * @param childURL 
     */
    public void addLinkToGraph(int parentID, int childID) throws Exception{
        linkGraph.addLink(parentID, childID);
    }

    /**
     * Run the thread.
     */
    @Override
    public void run() {
        while (true) {
            //List<URL> assignedURLs = new ArrayList<>(50);
            isWaitingForNewURLs = true;
            if (frontier.getQueueLength() > 0){
                URL curURL = frontier.getNext();
                System.out.println("Current URL: " + curURL.getURL());
                isWaitingForNewURLs = false;

                if (crawlerController.isShuttingDown()) {
                    System.out.println("Exiting because of controller shutdown.");
                    return;
                }
                //if (curURL != null) {
                try {
                    crawlPage(thread.getName(),curURL);
                } catch (Exception ex) {
                    continue;
//                        System.err.println("Thread " + thread.getName() + " encuntered an error crawling page " + curURL + ".");
//                        Logger.getLogger(WebCrawler.class.getName()).log(Level.SEVERE, null, ex);
                }
                //}
            }
            
        }
    }
}