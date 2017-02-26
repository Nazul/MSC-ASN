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

var AWS = require('aws-sdk');
AWS.config.loadFromPath('./config.json');

if (!AWS.config.credentials || !AWS.config.credentials.accessKeyId)
    throw 'Need to update config.json to specify your access key!';

// For AWS DynamoDB (cloud), use these lines
var db = new AWS.DynamoDB();
var docClient = new AWS.DynamoDB.DocumentClient();

// For DynamoDB Local, use these lines
//var db = new AWS.DynamoDB({endpoint: new AWS.Endpoint('http://localhost:8000')});
//var docClient = new AWS.DynamoDB.DocumentClient({endpoint: new AWS.Endpoint('http://localhost:8000')});

function keyvaluestore(table) {
    this.inx = -1;
    this.LRU = require("lru-cache");
    this.cache = this.LRU({max: 500});
    this.tableName = table;
}
;

/**
 * Initialize the tables
 * 
 */
keyvaluestore.prototype.init = function (callback) {
    var tableName = this.tableName;
    var initCount = this.initCount;
    var self = this;

    db.listTables(function (err, data) {
        if (err)
            console.log(err, err.stack);
        else {
            console.log("Connected to AWS DynamoDB");
            var tables = data.TableNames.toString().split(",");
            console.log("Tables in DynamoDB: " + tables);
            if (tables.indexOf(tableName) === -1) {
                console.log("Need to create table " + tableName);

                var params = {
                    AttributeDefinitions:
                            [/* required */
                                {
                                    AttributeName: 'keyword', /* required */
                                    AttributeType: 'S' /* required */
                                },
                                {
                                    AttributeName: 'inx', /* required */
                                    AttributeType: 'N' /* required */
                                }
                            ],
                    KeySchema:
                            [/* required */
                                {
                                    AttributeName: 'keyword', /* required */
                                    KeyType: 'HASH' /* required */
                                },
                                {
                                    AttributeName: 'inx', /* required */
                                    KeyType: 'RANGE' /* required */
                                }
                            ],
                    ProvisionedThroughput: {/* required */
                        ReadCapacityUnits: 1, /* required */
                        WriteCapacityUnits: 1 /* required */
                    },
                    TableName: tableName /* required */
                };

                db.createTable(params, function (err, data) {
                    if (err) {
                        console.log(err);
                    } else {
                        console.log("Waiting 10s for consistent state...");
                        setTimeout(function () {
                            self.initCount(callback);
                        }, 10000);
                    }
                });
            } else {
                self.initCount(callback);
            }
        }
    }
    );
};

/**
 * Gets the count of how many rows are in the table
 * 
 */
keyvaluestore.prototype.initCount = function (whendone) {
    var self = this;
    var params = {
        TableName: self.tableName,
        Select: 'COUNT'
    };

//    db.scan(params, function (err, data) {
//        if (err) {
//            console.log(err, err.stack);
//        } else {
//            self.inx = data.ScannedCount;
//
//            console.log("Found " + self.inx + " indexed entries in " + self.tableName);
//            whendone();
//        }
//    });

}

/**
 * Get result(s) by key
 * 
 * @param search
 * @param callback
 * 
 * Callback returns a list of objects with keys "inx" and "value"
 */
keyvaluestore.prototype.get = function (search, callback) {
    var params = {
        TableName: this.tableName,
        KeyConditionExpression: "#term = :kw",
        ExpressionAttributeNames: {
            "#term": "keyword"
        },
        ExpressionAttributeValues: {
            ":kw": search
        }
    };

    docClient.query(params, function (err, data) {
        if (err) {
            console.log(":( Unable to query. Error: ", JSON.stringify(err, null, 2));
        } else {
            console.log("Query '" + search + "' succeeded: " + data.Count + " record(s) found");
            console.log("\n");
            console.log(data.Items);
            console.log("\n");
        }
        callback(undefined, data.Items);
    });
};

/**
 * Test if search key has a match
 * 
 * @param search
 * @return
 */
keyvaluestore.prototype.exists = function (search, callback) {
    var self = this;

    if (self.cache.get(search))
        callback(null, self.cache.get(search));
    else
        module.exports.get(search, function (err, data) {
            if (err)
                callback(err, null);
            else
                callback(err, (data == null) ? false : true);
        });
};

/**
 * Get result set by key prefix
 * @param search
 *
 * Callback returns a list of objects with keys "inx" and "value"
 */
module.exports = keyvaluestore;

// EOF
