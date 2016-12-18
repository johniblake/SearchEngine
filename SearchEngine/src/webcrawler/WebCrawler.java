
package webcrawler;

import webpage.URL;
import fetcher.WebPageFetcher;
import indexer.ForwardIndex;
import frontier.FrontierQueue;
import java.util.*;
import java.io.IOException;
import org.jsoup.nodes.Document;
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
    protected ForwardIndex docStoreServer;
    private FrontierQueue URLServer;
    
    public void init(int id, WebCrawlerController crawlerController) throws IOException {
        this.myId = id;
        this.crawlerController = crawlerController;
        this.fetcher = new WebPageFetcher();
        this.docStoreServer = crawlerController.getStoreServer();
        this.URLServer = crawlerController.getURLServer();
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
        addPageToRepository(webPage);
        //send all URLs from webPage to docIDServer to add them to the anchors index/further process
//        Elements relativeLinks = webPage.getLinks();
//        ArrayList<String> links = new ArrayList<String>();
//        for (Element e:relativeLinks){
//            links.add(e.attr("abs:href"));
//        }
    }
    
    /**
     * Use ForwardIndex to save webpage to the repository as a (docID,document) pair 
     * @param links 
     */
    public void addPageToRepository(WebPage page){
        //queue jobs
    }
    
    /**
     * Use DocIDServer and URL Server to add link to frontier queue and the 
     * docID DB
     * @param url 
     */
    public void addURLToFrontier(URL url){
        
    }
    
    /**
     * Use LinkServer to add this edge to the graph of the web
     * @param curURL
     * @param childURL 
     */
    public void addLinkToGraph(URL curURL, URL childURL){
        
    }

    /**
     * Run the thread.
     */
    @Override
    public void run() {
        
    }
}