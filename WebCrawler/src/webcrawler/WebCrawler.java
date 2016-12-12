
package webcrawler;

import crawlerdata.*;
import org.jsoup.nodes.Element;

import webobjects.*;

/**
 * Each instance of this class is a web client that downloads web pages and stores them in the WebPageRepository.
 * @author johnblake
 */
public class WebCrawler {
    private static WebPage seed;
    private static FrontierQueue fq;
    private static FrontierList fd;
    
    /**
     * Initializes the crawl by downloading the first page and gathering a list of its child links.
     * @param seed String that represents the URL of the page from which the Crawler will begin its crawl.
     * @return WebPage 
     * @throws Exception 
     */
    public static WebPage initialize(String seed) throws Exception{
        Domain domain = new Domain(seed);
        Anchor anchor;
        //System.out.println("in initialize");
        //System.out.println("fd size: " + fd.hashtable.size());
        if (fd.hashtable.containsKey(domain.getDomainHash())){
            //System.out.println("domain in frontier");
            anchor = fd.hashtable.get(domain.getDomainHash());
            anchor.incrementHits();
            WebPage webPage = new WebPage(anchor);
            webPage.getDocumentFromWeb();
            return webPage;
        }
        //System.out.println("domain not in frontier");
        anchor = new Anchor(domain,seed);
//        fd.hashtable.put(domain.getDomainHash(), anchor);
//        fq.pq.add(domain);
        WebPage webPage = new WebPage(anchor);
        webPage.getDocumentFromWeb();
        //System.out.println("Got Document");
        return webPage;
    }
    
    public static void Step(WebPage parent){
        Domain childDomain;
        for (Element e:parent.getLinks()){
            System.out.println(e.attr("abs:href"));
            try {
                Thread.sleep(10);
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            try{
                WebPage child = initialize(e.attr("abs:href"));
                if (child != null){
                    //System.out.println("putting child");
                    childDomain = child.getAnchor().getDomain();
                    fd.hashtable.put(childDomain.getDomainHash(), child.getAnchor());
                    fq.pq.add(childDomain);
                    //add child to WebPage db
                } 
            }
            catch(Exception d){
                System.err.println("Child failed to initialize");
            }

        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        fq = new FrontierQueue();
        fd = new FrontierList();
        try{
            seed = initialize("http://www.jsoup.com/");
        }
        catch(Exception e){
            System.err.println("Seed failed to initialize");
            //System.err.println(e); 
        }
        if (seed != null){
            int depth = 0;
            Domain seedDomain = seed.getAnchor().getDomain();
            System.out.println("Seed Domain: "+seedDomain.getDomainUrl());
            fq.pq.add(seedDomain);
            while (!fq.pq.isEmpty()){
                depth +=1;
                System.out.println("Depth: " + depth);
                if (depth == 5){
                    System.out.println("Frontier Queue Size: " + fq.pq.size());
                    System.out.println("Frontier Queue: " + fq.pq.toString());
                    fq.pq.clear();
                }
                try {
                    String domainURL= fq.pq.poll().getDomainUrl();
                    System.out.println("Domain URL: "+ domainURL);
                    WebPage parent = initialize(domainURL);
                    System.out.println("initialized parent");
                    parent.getDocumentFromWeb();
//                    for (Element e:parent.getLinks()){
//                        System.out.println(e.attr("abs:href"));
//                    }
                    Step(parent);
                } catch (Exception ex) {
                    System.out.println("Failed at depth: " + depth);
                    System.err.println(ex);
                    //Logger.getLogger(PageCollector.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
}
