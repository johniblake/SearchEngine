var Redis = require('ioredis');
var MySQL = require('mysql');
var http = require('http');
var fs = require('fs');

//404 response
function send404Response(response) {
  response.writeHead(404,{"Context-Type": "text/html"});
  response.write("Error 404: Page not found!");
  response.end();
}

function onRequest(request, response) {
  console.log("A user made a request: " + request.url);
  if (request.method == 'GET' && request.url == '/'){
    response.writeHead(200,{"Context-Type": "text/html"});
    fs.createReadStream("./index.html").pipe(response);
  } else {
    send404Response(response);
  }
}

http.createServer(onRequest).listen(8888);
console.log("Server is now running...");

var forwardindex = MySQL.createConnection({
  host     : 'localhost',
  user     : 'root',
  password : 'focus1458',
  database : 'FORWARDINDEX'
});
var urlresolver = new Redis(6382, 'localhost');
var invertedindex = new Redis(6381,'localhost');
var searchtext = 'help me, please!';


function performSearch(searchtext) {
  var docID;
  var url;
  var textarray = searchtext.replace(/[^a-zA-Z]+/g," ").split(" ");
  console.log(textarray);
  for (i in textarray) {
    var word = textarray[i];
    if (word!=null) {
      wordSearch(word);
    }
  }
}

//perform search for individual word from query
function wordSearch(word) {
  var docIDs = [];
  var urls = [];
  invertedindex.smembers(word, function (err, result) {
    result.forEach(function(docID) {
      docIDs.push(docID);
      urlresolver.get(docID, function (err, result) {
        urls.push(result);
        //console.log(urls);
      });
    });
  });
  console.log(word);
  console.log(docIDs);
  console.log(urls);
}


performSearch(searchtext);
