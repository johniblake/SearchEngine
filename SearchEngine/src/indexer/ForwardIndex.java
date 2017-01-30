package indexer;

import webpage.URL;
import webpage.WebPage;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Stores webpages in the format (docID, html document)
 * @author johnblake
 */
public class ForwardIndex {
    //JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/FORWARDINDEX?zeroDateTimeBehavior=convertToNull";
    
    // Database Credentials
    static final String USER = "root";
    static final String PASS = "focus1458";
    static final Object INDEX_LOCK = new Object();
    public static Connection conn = null;

    
    
    public ForwardIndex(){
        Statement stmt = null;
        try{ 
            //register Driver class
            Class.forName(JDBC_DRIVER);
            //connect to database
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            //create page data table
//            String sql = "CREATE TABLE PAGEDATA(ID INT NOT NULL, TYPE CHAR(1) NOT NULL, TEXT TEXT NOT NULL, PRIMARY KEY (ID));";
//            stmt.executeUpdate(sql);
            System.out.println("Database created successfully...");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ForwardIndex.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error: unable to load driver class!");
            System.exit(1);
        } catch (SQLException ex) {
            Logger.getLogger(ForwardIndex.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error: Failed to establish SQL database connection!");
            System.exit(1);
        }//finally{
//            //finally block used to close resources
//            try{
//                if(stmt!= null){
//                  stmt.close();
//                }
//            }catch(SQLException se2){
//                //nothing we can do
//            }try{
//             if(conn!=null)
//                conn.close();
//            }catch(SQLException se){
//                se.printStackTrace();
//            }//end finally try
//        }
    }
    
    /**
     * Add a new (docID, htmldocument) pair to forward index
     * @param webpage 
     */
    public void addDocumentToDB(WebPage webpage, int id){
        String data = webpage.getBody();
        sqlCreate(id,'h', data);
    }
    /**
     * Add a new (docID, anchor text) pair to forward index
     * @param url
     */
    public void addAnchorToDB(URL url, int id){
        String data = url.getAnchorText();
        sqlCreate(id, 'a', data);
    }
    
    public void sqlCreate(int id, char datatype, String data){
        Statement stmt;
        try{
            stmt = conn.createStatement();
            String sql;
            sql = "INSERT INTO PAGEDATA VALUES ("+id+",'"+ datatype +"',\""+ data +"\");";
            stmt.executeUpdate(sql);
            stmt.close();
        }catch (SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }
    }
    
    public void closeConnection(){
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(ForwardIndex.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error: could not close SQL database connection!");
        }
    }
}
