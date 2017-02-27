package indexer;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import static org.neo4j.helpers.collection.MapUtil.map;




//import org.neo4j.helpers.collection.Iterators;

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
//        driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j","neo4j"));
//        System.out.println("Established Neo4j driver connection to " + driver);
        //File DB_FILE = new File("../../Documents/Neo4j/graph.db");
        //linkGraph = new EmbeddedGraphDatabase("Users/johnblake/Documents/Neo4j/graph.db");
        linkGraph = new GraphDatabaseFactory().newEmbeddedDatabase("../../Downloads/neo4j-community-2.2.3/data/graph.db");
        registerShutdownHook(linkGraph);
        
    }
    /**
     * This defines the types of relationships between nodes in the graph
     */
    private static enum RelTypes implements RelationshipType{
        PARENTOF
    }
    
    private static enum LabelTypes implements Label{
        NODE
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
        int initialPageRank = 0;
          
        
        // check if childDocID exists. If it does, add edge between parent node and child node. 
        // If not, make a new child node with the childDocID, and add edge between parent and child node.
        System.out.println("ADDING LINK TO GRAPH DB");  
        try (Transaction tx = linkGraph.beginTx()){
            System.out.println("Transaction has begun!");
            
            String query = "MATCH (n:Node {docID:{docID}} RETURN n AS node";
            ResourceIterator<Node> result1 = linkGraph.findNodes(LabelTypes.NODE, "docID", pDocID);
//            System.out.println("Got node result: " + result);
//            Map<String,Object> params = map("docID", pDocID);
//            try{
//                result1 = linkGraph.execute(query, params);
//                System.out.println(result1);
//            }catch(Exception e){
//                System.err.println("Error: could not execute cypher query.");
//            }
//            ResourceIterator<Node> n_columns = result1.columnAs("node");
            if (result1.hasNext()){
                System.out.println("Found node with ID: " + pDocID);
                firstNode = result1.next();
            }else{
                System.out.println("Result was null!");
                firstNode = linkGraph.createNode(LabelTypes.NODE);
                firstNode.setProperty("docID", pDocID);
                firstNode.setProperty("pagerank", initialPageRank);
                System.out.println("Created new node with ID: " + pDocID);
            }
            
            ResourceIterator<Node> result2 = linkGraph.findNodes(LabelTypes.NODE, "docID", cDocID);
            //n_columns = result2.columnAs("node");
            if (result2.hasNext()){
                System.out.println("Found node with ID: " + cDocID);
                secondNode = result2.next();
            }else{
                System.out.println("Result was null!");
                secondNode = linkGraph.createNode(LabelTypes.NODE);
                secondNode.setProperty("docID", cDocID);
                secondNode.setProperty("pagerank", initialPageRank);
                System.out.println("Created new node with ID: " + cDocID);
            }
            System.out.println("Looking for relationship ;)");
            Iterable<Relationship> relationships = firstNode.getRelationships();
            System.out.println("Got relationships list.");
            //Result result3 = linkGraph.execute("MATCH (a:Node)-[r:PARENTOF]->(b:Node) WHERE a.docID='"+pDocID+"' AND b.docID='"+cDocID+"' RETURN r");
            Iterator<Relationship> rel_list = relationships.iterator();
            boolean relationshipExists = false;
            while (rel_list.hasNext()){
                if (rel_list.next().getOtherNode(firstNode).equals(secondNode)){
                    System.out.println("Relationship already exists!");
                    relationshipExists = true;
                }      
            }
            if (!relationshipExists) {
                System.out.println("Relationship did not exist! Creating one...");
                relationship = firstNode.createRelationshipTo(secondNode, RelTypes.PARENTOF);
                System.out.println("Created relationship!");
            }
            tx.success();
        }
        // Check if parent and child node exist in graph. Add them if they dont, and create a relationship between them if none exists
        // for use with Bolt driver only.
//        Session session = driver.session();
//        StatementResult result = session.run("MATCH (n:Node) WHERE n.docID='"+pDocID+"' RETURN n AS node");
//        session.close();
//        if (!result.hasNext()){
//            session = driver.session();
//            session.run("CREATE (n:Node {docID:'"+pDocID+"',pagerank:"+initialPageRank+"})");
//            session.close();
//        }
//        session = driver.session();
//        result = session.run("MATCH (n:Node) WHERE n.docID='"+cDocID+"' RETURN n AS node");
//        session.close();
//        if (!result.hasNext()){
//            session = driver.session();
//            session.run("CREATE (n:Node {docID:'"+cDocID+"',pagerank:"+initialPageRank+"})");
//            session.close();
//        }
//        session = driver.session();
//        result = session.run("MATCH (a:Node)-[r:PARENTOF]->(b:Node) WHERE a.docID='"+pDocID+"' AND b.docID='"+cDocID+"' RETURN r");
//        session.close();
//        if (!result.hasNext()){
//            session = driver.session();
//            session.run("MATCH (a:Node), (b:Node) WHERE a.docID='"+pDocID+"' AND b.docID='"+cDocID+"' CREATE (a)-[r:PARENTOF]->(b)");
//            session.close();
//        }
        
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
