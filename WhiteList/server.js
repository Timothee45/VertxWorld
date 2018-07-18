var http = require('http');
var url = require('url');
var request = require('request');
var querystring = require('querystring');

var whiteList = ["titi", "toto", "tata"];

var server = http.createServer(function(req, res) {
	var page = url.parse(req.url).pathname;
	var params = querystring.parse(url.parse(req.url).query);

	var resultData = {available : false};

	if (page == '/auth') {
		if ('user' in params) {
			resultData = {available : whiteList.includes(params['user'])};
		}
	}

	res.write(JSON.stringify(resultData));

 	res.end();
});

server.listen(8080);
