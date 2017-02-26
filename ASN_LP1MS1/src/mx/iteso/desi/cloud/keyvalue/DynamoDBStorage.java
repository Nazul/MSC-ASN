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
package mx.iteso.desi.cloud.keyvalue;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.BatchWriteItemRequest;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutRequest;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import mx.iteso.desi.cloud.hw1.Config;

public class DynamoDBStorage extends BasicKeyValueStore {

    String dbName;
    // Simple autoincrement counter to make sure we have unique entries
    int inx;
    //Set<Map<String, AttributeValue>> attributesToGet = new HashSet<>();
    List<Map<String, AttributeValue>> attributes = new ArrayList<>();
    //Map<String, List<WriteRequest>> requestItems = new HashMap<>();
    //List<WriteRequest> requestList = new ArrayList<>();
    // AWS DynamoDB objects
    AmazonDynamoDB client;
    DynamoDB docClient;
    // Batch size
    private final int BATCH_SIZE = 15;

    public DynamoDBStorage(String dbName) {
        BasicAWSCredentials cred = new BasicAWSCredentials(Config.accessKeyID, Config.secretAccessKey);

        if (Config.DynamoDbClientType == Config.DYNAMODBCLIENTTYPE.Local) {
            client = new AmazonDynamoDBClient(cred);
            client.setRegion(Region.getRegion(Config.amazonRegion));
            client.setEndpoint("http://localhost:8000");
        } else {
            client = AmazonDynamoDBClientBuilder.standard()
                    .withRegion(Config.amazonRegion)
                    .withCredentials(new AWSStaticCredentialsProvider(cred))
                    .build();
        }
        docClient = new DynamoDB(client);

        this.dbName = dbName;
        // Create a table
        CreateTableRequest createTableRequest = new CreateTableRequest().withTableName(dbName)
                .withKeySchema(new KeySchemaElement().withAttributeName("keyword").withKeyType(KeyType.HASH),
                        new KeySchemaElement().withAttributeName("inx").withKeyType(KeyType.RANGE))
                .withAttributeDefinitions(new AttributeDefinition().withAttributeName("keyword").withAttributeType(ScalarAttributeType.S),
                        new AttributeDefinition().withAttributeName("inx").withAttributeType(ScalarAttributeType.N))
                .withProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits(1L).withWriteCapacityUnits(1L));
        // Create table if it does not exist yet
        TableUtils.createTableIfNotExists(client, createTableRequest);
        // Wait for the table to move into Active state
        try {
            TableUtils.waitUntilActive(client, dbName);
        } catch (Exception e) {
            // Do nothing... yet
        }
        //requestItems.put(dbName, requestList);
    }

    @Override
    public Set<String> get(String search) {
        Set<String> ret = new HashSet<String>();
        Table table = docClient.getTable(dbName);

        QuerySpec spec = new QuerySpec()
                .withKeyConditionExpression("keyword = :v_kw")
                .withValueMap(new ValueMap()
                        .withString(":v_kw", search));

        ItemCollection<QueryOutcome> items = table.query(spec);

        Iterator<Item> iterator = items.iterator();
        Item item = null;
        while (iterator.hasNext()) {
            item = iterator.next();
            ret.add(item.get("value").toString());
        }
        return ret;
    }

    @Override
    public boolean exists(String search) {
        for (Map<String, AttributeValue> attribute : attributes) {
            if (attribute.get("keyword").getS().equals(search)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Set<String> getPrefix(String search) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addToSet(String keyword, String value) {
        put(keyword, value);
    }

    @Override
    public void put(String keyword, String value) {
//        try {
//            Map<String, AttributeValue> item = newItem(keyword, value);
//            PutItemRequest putItemRequest = new PutItemRequest(dbName, item);
//            PutItemResult putItemResult = client.putItem(putItemRequest);
//            System.out.println("Result: " + putItemResult);
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
        attributes.add(newItem(keyword, value));
        //requestList.add(new WriteRequest(new PutRequest(newItem(keyword, value))));
    }

    private Map<String, AttributeValue> newItem(String key, String value) {
        Map<String, AttributeValue> item = new HashMap<>();
        AttributeValue av = new AttributeValue();

        item.put("keyword", new AttributeValue(key));
        item.put("inx", new AttributeValue().withN(Integer.toString(++inx)));
        item.put("value", new AttributeValue(value));

        return item;
    }

    @Override
    public void close() {
        System.out.println("** CLOSING **");
        this.sync();

        dbName = "";
        inx = 0;
        attributes = new ArrayList<>();
        //requestItems = new HashMap<>();
        //requestList = new ArrayList<>();
        client = null;
        docClient = null;
        //requestItems.put(dbName, requestList);
    }

    @Override
    public boolean supportsPrefixes() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void sync() {
        Map<String, List<WriteRequest>> requestItems = new HashMap<>();
        List<WriteRequest> requestList = new ArrayList<>();
        requestItems.put(dbName, requestList);

        for (int i = 0; i < attributes.size(); i++) {
            if (i % BATCH_SIZE == 0) {
                requestList.add(new WriteRequest(new PutRequest(attributes.get(i))));
                BatchWriteItemRequest bwir = new BatchWriteItemRequest(requestItems);
                System.out.println("Result: " + client.batchWriteItem(bwir));
                requestItems = new HashMap<>();
                requestList = new ArrayList<>();
                requestItems.put(dbName, requestList);
            } else {
                requestList.add(new WriteRequest(new PutRequest(attributes.get(i))));
            }
        }
        if (requestList.size() > 0) {
            BatchWriteItemRequest bwir = new BatchWriteItemRequest(requestItems);
            System.out.println("Result: " + client.batchWriteItem(bwir));
            requestItems = new HashMap<>();
            requestList = new ArrayList<>();
            requestItems.put(dbName, requestList);
        }
    }

    @Override
    public boolean isCompressible() {
        return false;
    }

    @Override
    public boolean supportsMoreThan256Attributes() {
        return true;
    }
}

// EOF
