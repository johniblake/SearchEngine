var express = require('express');
var redis = require('ioredis');
var neo4j = require('neo4j');
var fs = require('fs');
var async = require('async');
var router = express.Router();


var invertedindex = new redis('6381','localhost');
var urlresolver = new redis('6382','localhost');
var titleIndex = new redis('6383', 'localhost');
var graphdb = new neo4j.GraphDatabase('http://neo4j:focus1458@localhost:7474');


/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', {
    title: 'CarlSearch'
  });
});
router.get('/about', function(req, res, next) {
  res.render('about', {
    title: 'About'
  })
});

router.get('/search', function(req, res) {
  var stack = [];
  var finalresponse = [];
  query = req.query.typeahead.replace(/[^a-zA-Z]+/g, " ").split(" ");
  querycopy = [];
  for(i in query){
    querycopy.push(query[i]);
  }
  console.log(query);

  //push each search term to the stack to be processed

  function performSearch (word, callback) {
    //console.log("Word: " + word);
    //get the doc id's associated with a given word
    invertedindex.smembers(word, function (err, docIDs) {
      //get the urls associated with the list of docID's
      urlresolver.mget(docIDs, function (err, urls) {
        titleIndex.mget(docIDs, function (err, titles) {
          if (urls == undefined) {
            urls = [];
          }
          if (titles == undefined) {
            titles = [];
          }
          queryStack = [];
          function queryGraphDB (docID, callback) {
            graphdb.cypher({
              query: 'MATCH (n:NODE) WHERE n.docID='+docID+' RETURN n.pagerank AS pagerank',
              params: {
              },
            }, function (err, results) {
                if (!results) {
                  console.log('No pagerank found.');
                  callback(null,null);
                } else {
                  var result = results[0];
                  var pagerank = result['pagerank'];
                  callback(null,JSON.stringify(pagerank, null, 4));
                }
            });
          }
          for (i in docIDs) {
            queryStack.push(queryGraphDB.bind(null,docIDs[i]));
          }
          async.parallel(queryStack, function(error, result) {
            var page = {
              "docID" : "",
              "title" : "",
              "url" : "",
              "pagerank" : ""
            };
            response = {
              "word": "",
              "pages": []
            };
            response.word = word;

            for (i in docIDs) {
              var newPage = Object.create(page);
              newPage.docID = docIDs[i];
              newPage.url = urls[i];
              newPage.pagerank = result[i];
              newPage.title = titles[i];
              response.pages.push(newPage);
            }
            if (docIDs != []){
              callback(null,response);
            }else{
              callback(null,null);
            }
          })
        });
      });
    });
  };

  for (index in query) {
    stack.push(performSearch.bind(null,query[index]));
  }
  //function modified from StackOverflow post by Anurag
  function getSubsets(list, min) {
    var fn = function(n, src, got, all){
      if (n == 0) {
        if (got.length > 0) {
          all[all.length] = got;
        }
        return;
      }
      for (var j = 0; j < src.length; j++) {
        fn(n - 1, src.slice(j + 1), got.concat([src[j]]), all);
      }
      return;
    }
    var all = [];
    for (var i = min; i < list.length; i++) {
      fn(i, list, [], all);
    }
    all.push(list);
    return all;
  }

  function contains(list, url){
    //console.log("Contains List: " + list);
    for (i in list){
      if (url == list[i].url){
        return true;
      }
    }
    //console.log("returning false");
    return false;
  }

  function getIntersection(list){
    var result = [];
    var argslength = list.length;

    if (argslength == 0) return [];
    else if (argslength == 1) return list[0][0];

    for (var i = 0; i < list[0][0].length; i++) {
      var curItem = list[0][0][i];
      var curUrl = list[0][0][i].url;
      if(contains(result, curUrl)) {
        continue;
      }
      var j;
      for (j = 1; j < argslength; j++) {
        if (!contains(list[j][0], curUrl)) {
          break;
        }
      }
      if (j == argslength){
        result.push(curItem);
      }
    }
    return result;
  }
  // Takes in a list of {word, pages} pairs and outputs the
  function processPhrase(list) {
    var phrase = "";
    var pageSets = [];
    for (i in list) {
      item = list[i];
      var word = item.word + " ";
      phrase += word;
      var pages = item.pages
      pageSets[i]= [pages];
    }
    var pages = mergeSortByPageRank(getIntersection(pageSets));

    return {
      "phrase" : phrase,
      "pages" : pages
    };
  }

  function mergeSortByPageRank(pages) {
    if (pages.length < 2) {
      return pages;
    }
    var middle = parseInt(pages.length/2);
    var left = pages.slice(0,middle);
    var right = pages.slice(middle, pages.length);
    return merge(mergeSortByPageRank(left), mergeSortByPageRank(right));
  }

  function merge(left, right) {
    var result = [];

    while (left.length && right.length) {
      if (left[0].pagerank >= right[0].pagerank) {
        result.push(left.shift());
      }else{
        result.push(right.shift());
      }
    }
    while (left.length) {
      result.push(left.shift());
    }
    while (right.length) {
      result.push(right.shift());
    }
    return result;
  }

  //async.parallel syncronizes a stack of functions. In this case each function is a
  async.parallel(stack, function (error, result) {
    resSubsets = getSubsets(result,result.length-2);
    results = [];
    for (i in resSubsets) {
      results.unshift(processPhrase(resSubsets[i]));
    }
    res.render('search', {
      items: results,
      title: 'CarlSearch'
    });
  });
});

module.exports = router;
