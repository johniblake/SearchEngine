package indexer;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import static org.neo4j.driver.v1.Values.parameters;

/**
 * Builds a graph of (url, parent set, child set) tuples to be iterated over in order to calculate
 * PageRank
 * @author johnblake
 */
public class LinkGraph {
    
    Driver driver;
    
    /**
     * empty constructor
     */
    public LinkGraph(){
        driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "password"));
        
    }
    
    /**
     * add edge to graph if it does not already exist
     * @param url
     * @param parentURL 
     */
    public void addLink(int parentDocID, int childDocID){
        String cDocID = Integer.toString(childDocID);
        String pDocID = Integer.toString(parentDocID);
        String initialPageRank = "0";
        try (Session session = driver.session()){
            try (Transaction tx = session.beginTransaction()) {
                
                tx.run("CREATE (id:P"+pDocID+" {pageRank: {pageRank}})", parameters("pageRank", initialPageRank));
                tx.success();
            }
            try(Transaction tx = session.beginTransaction()) {
                StatementResult result = tx.run("MATCH (id:P"+pDocID+") WHERE id.pageRank = {pageRank} " + "RETURN id.pageRank AS pageRank", parameters("pageRank", initialPageRank));
                while (result.hasNext()) {
                    Record record = result.next();
                    System.out.println(String.format("%s", record.get("pageRank").asString()));
                }
                tx.success();
            }          
        }
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
