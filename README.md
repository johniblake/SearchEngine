# SearchEngine

Turn in 1

CREATE TABLE statements:

Frontier Queue: Redis

        RedisClient frontierQueue = new RedisClient();
        RedisConnection<int, String> connection = frontierQueue.connect();
        connection.set(priority, url);
        connection.close();
        redisClient.shutdown()
        
  

Document ID DataBase (Document Index): Redis

        RedisClient docIndex = new RedisClient();
        RedisConnection<String, int> connection = docIndex.connect();
        connection.set(url, docID);
        connection.close();
        redisClient.shutdown()
        
Links Database: Redis
        RedisClient linksDB = new RedisClient();
        RedisConnection<String, List<String>,List<String>> connection = linksDB.connect();
        connection.set(curURL, null , childUrl);
        connection.set(childURL, currURL, null);
        connection.close();
        redisClient.shutdown()
        
 Why Redis? Redis is an in-memory database system that supports data persistence, making it both fast and crash resistent. Periodic snapshots of the database can be saved to memory so that if the crawler ever needs to exit or if it crashes, startup can happen from the last snapshot.
 Sources: 
 https://product.reverb.com/a-simple-priority-queue-with-redis-in-ruby-7e3ec780f237#.ahhs2qvq0
 http://blog.marc-seeger.de/assets/papers/thesis_seeger-building_blocks_of_a_scalable_webcrawler.pdf
 https://redislabs.com/why-redis
 
 ForwardIndex: InnoDB
 
  CREATE TABLE t1 (docID INT, htmldocument TEXT (20), PRIMARY KEY (a));
  
  Why InnoDB? InnoDB has fast write capabilities, which it perfect for the forward index. 
  
  Source on DBs: 
  
  https://dev.mysql.com/doc/refman/5.6/en/innodb-fulltext-index.html
  http://blog.marc-seeger.de/assets/papers/thesis_seeger-building_blocks_of_a_scalable_webcrawler.pdf
  
  InvertedIndex: Solr
  
  Creating a solr index: https://docs.datastax.com/en/datastax_enterprise/4.5/datastax_enterprise/srch/srchIdx.html
  
  Why Solr? 
  Sources: 
  http://blog.marc-seeger.de/assets/papers/thesis_seeger-building_blocks_of_a_scalable_webcrawler.pdf
  http://lucene.apache.org/solr/features.html
 
 
