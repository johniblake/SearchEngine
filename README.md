# Back End Installation and Deployment

## 1) Clone this repository.

## 2) Download the following JARs and add them to your classpath:
- [JSoup](http://central.maven.org/maven2/org/jsoup/jsoup/1.10.1/jsoup-1.10.1.jar)
- [JSoup Javadoc](https://jsoup.org/packages/jsoup-1.10.1-javadoc.jar)
- [Redis](http://central.maven.org/maven2/redis/clients/jedis/2.4.2/jedis-2.4.2.jar)
- [Neo4J](http://central.maven.org/maven2/org/neo4j/neo4j/2.2.3/neo4j-2.2.3.jar)
- [MySQL Connector](https://mvnrepository.com/artifact/mysql/mysql-connector-java/5.1.23)

        
## 3) Download and install Docker:
- [Docker for Mac] (https://download.docker.com/mac/stable/Docker.dmg)

## 4) Run Docker and execute the following commands from within Terminal:

- docker pull redis
- docker pull sequenceiq/hadoop-docker:2.4.1
- docker pull kbastani/docker-neo4j:2.2.1
- docker pull kbastani/neo4j-graph-analytics:1.1.0

## 5) Create Redis Databases
- docker run --name redis-frontierindex -d -p 6379:6379 redis
- docker run --name redis-docindex -d -p 6379:6380 redis
- docker run --name redis-urlresolver -d -p 6379:6381 redis
- docker run --name redis-titleindex -d -p 6379:6382 redis

## 6) Create HDFS
docker run -i -t --name hdfs sequenceiq/hadoop-docker:2.4.1 /etc/bootstrap.sh -bash

## 7) Create Mazerunner Apache Spark Service
docker run -i -t --name mazerunner --link hdfs:hdfs kbastani/neo4j-graph-analytics:1.1.0

## 8) Create Neo4j database with links to HDFS and Mazerunner
 Replace <user> and <neo4j-path> with the location to your existing Neo4j database store directory:
- docker run -d -P -v /Users/<user>/<neo4j-path>/data:/opt/data --name graphdb --link mazerunner:mazerunner --link hdfs:hdfs kbastani/docker-neo4j:2.2.1


## 9) Run the project!
```
NOTE: To Run the project at this point run the WebCrawlerController.java file located in the webcrawler package. I'm still working out the kinks, so for now stop the project manually when you are done. Check the various databases to view the results of your crawl.
```  

# Front End Installation and Deployment

## 1) [Download and Install Node.js](https://nodejs.org/dist/v6.11.3/node-v6.11.3.pkg)

## 2) In Terminal, navigate to the directory containing package.json (../SearchEngine/FrontEnd/searchserver/) and execute the command 'npm install' to download install all dependecies.
 
## 3) Use command 'node start' to start the server.
 
## 4) Open browser and navigate to localhost:3000 to interact with the front end.



 
