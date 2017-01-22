package indexer;

import java.io.File;
import java.util.Iterator;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.helpers.collection.Iterators;

/**
 * Builds a graph of (url, parent set, child set) tuples to be iterated over in order to calculate
 * PageRank
 * @author johnblake
 */
public class LinkGraph {
    
    GraphDatabaseService linkGraph;
    Node firstNode;
    Node secondNode;
    Relationship relationship;
    File DB_FILE;
    /**
     * empty constructor
     */
    public LinkGraph(){
        File DB_FILE = new File("../../Documents/Neo4j/default.graphdb");
        linkGraph = new GraphDatabaseFactory().newEmbeddedDatabase(DB_FILE);
        registerShutdownHook(linkGraph);
        
    }
    /**
     * This defines the types of relationships between nodes in the graph
     */
    private static enum RelTypes implements RelationshipType{
        PARENTOF
    }
    
    /**
     * Registers a shutdown hook for the Neo4j instance so that it shuts down 
     * nicely when the VM exits (even if you "Ctrl-C" the running application).
     * @param linkGraph 
     */
    public static void registerShutdownHook(final GraphDatabaseService linkGraph){
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                linkGraph.shutdown();
            }
        });
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
        // check if childDocID exists. If it does, add edge between parent node and child node. 
        // If not, make a new child node with the childDocID, and add edge between parent and child node.
        
        try (Transaction tx = linkGraph.beginTx()){
            Result result1 = linkGraph.execute("MATCH (n {docID:'"+pDocID+"'}) RETURN n, n.docID");
            Iterator<Node> n_columns = result1.columnAs("n");
            if (n_columns.hasNext()){
                for (Node node:Iterators.asIterable(n_columns)){
                    firstNode = node;
                }
            }else{
                firstNode = linkGraph.createNode();
                firstNode.setProperty("docID", pDocID);
            }
            
            Result result2 = linkGraph.execute("MATCH (n {docID:'"+cDocID+"'}) RETURN n, n.docID");
            n_columns = result2.columnAs("n");
            if (n_columns.hasNext()){
                for (Node node:Iterators.asIterable(n_columns)){
                    secondNode = node;
                }
            }else{
                secondNode = linkGraph.createNode();
                secondNode.setProperty("docID", cDocID);
            }
            
            Result result3 = linkGraph.execute("MATCH (r {docID:'"+pDocID+"'}) -- ({docID:'"+cDocID+"'}) RETURN r");
            n_columns = result3.columnAs("r");
            if (!n_columns.hasNext()){
                relationship = firstNode.createRelationshipTo(secondNode, RelTypes.PARENTOF);
            }
            tx.success();
        }
    }
    
    /**
     * recurse over database using BFS, calculating and storing PageRank for each
     * child URL
     */
//    public void recurse(String startURL){
//        
//    }
    
    /**
     * gets the pagerank of the url from the Links database
     * @param url
     * @return 
     */
    public int getPageRank(String url){
        int pagerank = 0;
        return pagerank;
    }
    
    public void shutDown(){
        linkGraph.shutdown();
    }
    
}
