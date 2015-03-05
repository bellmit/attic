'use strict';

console.log('login-box.boot');
process
    .on('SIGINT', function() {
        console.log('login-box.shutdown');
        server.close();
    })
    .on('SIGTERM', function() {
        console.log('login-box.shutdown');
        server.close();
    });

var express = require('express');
var http = require('http');
var program = require('commander');

program
    .option('-p, --port [PORT]', 'Server listen port', process.env['PORT'] || 5000)
    .parse(process.argv);

var app = express();
var server = http.createServer(app);

server
    .on('listening', function() {
        console.log('login-box.server.listening port=%s', program.port);
    })
    .on('close', function() {
        console.log('login-box.server.unlisten');
    });

server
    .listen(program.port);
