# Getting Started

1) Clone this repository.

2) Download the following JARs and add them to your classpath:
- [JSoup] (http://central.maven.org/maven2/org/jsoup/jsoup/1.10.1/jsoup-1.10.1.jar)
- [JSoup Javadoc] (https://jsoup.org/packages/jsoup-1.10.1-javadoc.jar)
- [Redis] (http://central.maven.org/maven2/redis/clients/jedis/2.4.2/jedis-2.4.2.jar)
- [Neo4J] (http://central.maven.org/maven2/org/neo4j/neo4j-jdbc/3.0.1/neo4j-jdbc-3.0.1.jar)
- [MySQL Connector] (https://mvnrepository.com/artifact/mysql/mysql-connector-java/5.1.23)

        
3) Follow these directions to complete the Neo4j setup:
```
        https://www.tutorialspoint.com/neo4j/neo4j_java_environment_setup.htm
```        
4) Install MySQL:
```
        https://dev.mysql.com/doc/refman/5.6/en/osx-installation-pkg.html
```
5) Install Redis and set up a second instance to run alongside the first:
```
        http://www.codexpedia.com/devops/install-redis-and-set-multiple-redis-instances-on-mac-os/
        NOTE: the Frontier Queue uses port 6379 and the Document Index uses port 6380
```        
6) Intall Neo4J Community Edition & Redis Desktop Manager:

- [Neo4J Community Edition](https://neo4j.com/download/)
- [Redis Desktop Manager](https://redisdesktop.com/) (Optional but nice to have)
```
        NOTE: For Redis Desktop Manager you will be required to buy a subscription. Ask me for my login info to complete this step.
```
7) Start all databases.
```
        Neo4j: This database does not require running beforehand. Run Neo4J Community Edition in order to view the graph in your browser,                  but make sure it is shutdown before executing my code.
        Redis: In two fresh terminal windows execute the following commands: 
                redis-server /usr/local/etc/redis.conf
                redis-server /usr/local/etc/redis2.conf
        MySQL: Open System Preferences and click on the MySQL icon. Hit the 'Start MySQL Server' button.
               NOTE: you may be required to configure your own login credentials, but if that fails mine are: 
               user:'root' 
               pass:'focus1458'
```
8) Run the project!
```
        NOTE: To Run the project at this point run the WebCrawlerController.java file located in the webcrawler package. I'm still working out the kinks, so for now stop the project manually when you are done. Check the various databases to view the results of your crawl.
```  



 
