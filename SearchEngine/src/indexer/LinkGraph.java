package indexer;

import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.*;




//import org.neo4j.helpers.collection.Iterators;

/**
 * Builds a graph of (url, parent set, child set) tuples to be iterated over in order to calculate
 * PageRank
 * @author johnblake
 */
public class LinkGraph {
    File DB_FILE;
    String DB_URL;
    
    /**
     * empty constructor
     */
    public LinkGraph(){
        DB_URL = "http://localhost:7474/";
        authenticateConnection();
        
    }
    
    public void authenticateConnection(){
        try {
            // create the HttpURLConnection
            URL url = new URL(DB_URL + "/user/neo4j");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // just want to do an HTTP GET here
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json; charset=UTF-8");
            connection.setRequestProperty("Authorization", "Basic bmVvNGo6Zm9jdXMxNDU4");
            connection.setDoOutput(true);
            connection.setReadTimeout(5000);
            connection.connect();
            connection.disconnect();
        } catch (Exception e) {
            System.out.println("Could not authenticate connection!");
        }
    }
    
    public JSONObject queryGraph(String cypherQuery) throws Exception{
        URL url = null;
        BufferedReader reader = null;    
        StringBuilder stringBuilder;
        String json = "{\"statements\":[{\"statement\":\""+cypherQuery+"\",\"includeStats\":false}]}";
        JSONObject jsonObj = new JSONObject(json);

        try {
            // create the HttpURLConnection
            url = new URL(DB_URL + "db/data/transaction/commit");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // just want to do an HTTP POST here
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json; charset=UTF-8");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Basic bmVvNGo6Zm9jdXMxNDU4");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setReadTimeout(5000);
            OutputStream os = connection.getOutputStream();
            os.write(jsonObj.toString().getBytes("UTF-8"));
            os.flush();
            os.close();
            
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            stringBuilder = new StringBuilder();
            String line = null;
            
            while ((line = reader.readLine()) != null) {
                //System.out.println(line);
                stringBuilder.append(line + "\n");
            }            
            //create JSON object from string response
            jsonObj =new JSONObject(stringBuilder.toString());
            connection.disconnect();
            //Return JSON response
            return jsonObj;    
            
        } catch (Exception e) {
            System.out.println("Error executing cypher query.");
            e.printStackTrace();
            throw e;
        } finally {
          // close the reader; this can throw an exception too, so
          // wrap it in another try/catch block.
            if (reader != null) {
                try{
                    reader.close();
                }catch (IOException ioe) {
                    System.out.println("Error closing reader in LinkGraph");
                    ioe.printStackTrace();
                }
            }
        }
    }
    
    public boolean nodeExists(JSONObject queryResponse){
        if (queryResponse.getJSONArray("results").getJSONObject(0).getJSONArray("data").toString().length() > 2) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean relationshipExists(JSONObject queryResponse){
        if (queryResponse.getJSONArray("results").getJSONObject(0).getJSONArray("data").toString().length() > 2) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * add edge to graph if it does not already exist
     * @param url
     * @param parentURL 
     */
    public void addLink(int parentDocID, int childDocID) throws Exception{
        String cDocID = Integer.toString(childDocID);
        String pDocID = Integer.toString(parentDocID);
//        pDocID = "1";
//        cDocID = "2";
        double initialPageRank = 0.15;
        
       
        //Cypher Get Statements
//        System.out.println("ADDING LINK TO GRAPH DB");  
        String getParent = "MATCH (n:NODE) WHERE n.docID="+ pDocID +" RETURN n";
        String getChild = "MATCH (n:NODE) WHERE n.docID="+ cDocID +" RETURN n";
        String getRelationship = "MATCH (n:NODE)-[r:PARENTOF]->(m:NODE) WHERE n.docID="+ pDocID +" AND m.docID="+ cDocID +" RETURN r";
        
        //Cypher Create Statements
        String createParent = "CREATE (n:NODE {docID:"+pDocID+",pagerank:"+initialPageRank+"})";
        String createChild = "CREATE (n:NODE {docID:"+cDocID+",pagerank:"+initialPageRank+"})";
        String createRelationship = "MATCH (n:NODE), (m:NODE) WHERE n.docID="+ pDocID +" AND m.docID ="+ cDocID +" CREATE (n)-[r:PARENTOF]->(m)";
        
        //Check if parent and child nodes exists. If they do not, create them.
        JSONObject parentNode = queryGraph(getParent);
        JSONObject childNode = queryGraph(getChild);
        try {
            if (nodeExists(parentNode)){
//                System.out.println("Parent node exists!");
            }else{
//                System.out.println("Parent node does not exist! Creating it...");
                parentNode = queryGraph(createParent);
            }
            if (nodeExists(childNode)){
//                System.out.println("Child node exists!");
            }else{
//                System.out.println("Child node does not exist! Creating it...");
                parentNode = queryGraph(createChild);
            }
            // Check if a link between the parent and child has been established. If it has not, create one.
            JSONObject linkToChild = queryGraph(getRelationship);
            if (!relationshipExists(linkToChild)){
//                System.out.println("Relationship does not exist! Creating it...");
                linkToChild = queryGraph(createRelationship);
            }
         
        } catch (Exception e){
            System.out.println("Error adding link.");
            e.printStackTrace();
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
    public String getPageRank(String docID) throws Exception{
        String getNode = "MATCH (n:NODE) WHERE n.docID="+ docID +" RETURN n";
        JSONObject node = queryGraph(getNode);
        String pagerank = "0";
        try{
            pagerank = node.getJSONArray("results").getJSONObject(0).getJSONArray("data").getJSONObject(0).getJSONArray("row").getJSONObject(0).get("pagerank").toString();       
            return pagerank;
        } catch (Exception e){
            return Integer.toString(0);
        }
    }
    public void calculatePageRanks(){
        String calculatePageRank = "";
        try {
            queryGraph(calculatePageRank);
        } catch (Exception ex) {
            Logger.getLogger(LinkGraph.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }
    
//    public void shutDown(){
//        linkGraph.shutdown();
//    }
    
}
