/* 
 * Copyright 2017 Mario Contreras <marioc@nazul.net>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// Modules
var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');
var stemmer = require('porter-stemmer').stemmer;
var async = require('async');

//Own Modules
var aws = require('./keyvaluestore.js');

// Express
var app = express();


// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'jade');


app.use(logger('dev'));

app.use(function (req, res, next) {
    res.setHeader("Cache-Control", "no-cache must-revalidate");
    next();
});

app.use(express.static(path.join(__dirname, 'public')));

// Routes
app.get('/', function (req, res, next) {
    res.render('index', {title: 'Express'});
});

app.get('/search/:word', function (req, res) {
    var stemmedword = stemmer(req.params.word).toLowerCase(); //stem the word
    console.log("Stemmed word: " + stemmedword);

    var imageurls = new Array();

    var processData = function (callback) {
        terms.get(stemmedword, function (err, data) {
            if (err) {
                console.log("terms.get() failed: " + err);
                callback(err.toString(), imageurls);
            } else if (data == null) {
                console.log("terms.get() returned no results");
                callback(undefined, imageurls);
            } else {
                console.log("terms.get() returned " + data.count + " record(s)");
                console.log(data);
                async.forEach(data, function (attribute, callback) {
                    console.log("Searching for images:\n");
                    console.log(attribute.value);
                    images.get(attribute.value, function (err, data) {
                        if (err) {
                            console.log(err);
                        }
                        imageurls.push(data[0].value);
                        callback();
                    });
                }, function () {
                    callback(undefined, imageurls);
                });
            }
        });
    };

    processData(function (err, queryresults) {
        if (err) {
            res.send(JSON.stringify({results: undefined, num_results: 0, error: err}));
        } else {
            res.send(JSON.stringify({results: queryresults, num_results: queryresults.length, error: undefined}));
        }
    });
});

//INIT Logic
var images = new aws('images');
var terms = new aws('terms');

images.init(
        function () {
            terms.init(
                    function () {
                        console.log("Images Storage Started\n");
                    }
            );
            console.log("Terms Storage Started\n");
        }
);


module.exports = app;

// EOF
