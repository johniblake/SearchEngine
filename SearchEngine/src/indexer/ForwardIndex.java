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
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/FORWARDINDEX?zeroDateTimeBehavior=convertToNull";
    private static final String BAD_CHARS = "[^a-zA-Z0-9]+";

    
    
    // Database Credentials
    static final String USER = "root";
    static final String PASS = "focus1458";
    static final Object INDEX_LOCK = new Object();
    public static Connection conn = null;

    
    
    public ForwardIndex(){
        Statement stmt = null;  
        initDB();
        try{ 
            //register Driver class
            Class.forName(JDBC_DRIVER);
            //connect to database
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            System.out.println("Database created successfully...");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ForwardIndex.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error: unable to load driver class!");
            System.exit(1);
        } catch (SQLException ex) {
            Logger.getLogger(ForwardIndex.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error: Failed to establish SQL database connection!");
            System.exit(1);
        }
    }
    
    /**
     * Add a new (docID, htmldocument) pair to forward index
     * @param webpage 
     */
    public void addDocumentToDB(WebPage webpage, int id){
        String data = webpage.getBody();
        data = data.replaceAll(BAD_CHARS, " ");
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
        String sql;
        sql = "INSERT INTO PAGEDATA VALUES ("+id+",'"+ datatype +"',\""+ data +"\");";
        try{
            stmt = conn.createStatement(); 
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
    
    public void initDB(){
        Statement stmt = null;
         // SQL command to create a database in MySQL.
        String createDB = "CREATE DATABASE IF NOT EXISTS FORWARDINDEX";
        String createTable = "CREATE TABLE PAGEDATA(ID INT NOT NULL, TYPE CHAR(1) NOT NULL, TEXT TEXT NOT NULL, PRIMARY KEY (ID));";
        String url= "jdbc:mysql://localhost:3306";

        try{
            Connection conn = DriverManager.getConnection(url, USER, PASS);
            stmt = conn.createStatement();
            stmt.executeUpdate(createDB);
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        try{
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            stmt.executeUpdate(createTable);
            stmt.close();
        }catch(Exception f) {
            System.out.println("Table PAGEDATA already exists!");
        }
    }
}
