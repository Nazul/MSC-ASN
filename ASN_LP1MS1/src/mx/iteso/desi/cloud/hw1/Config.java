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
package mx.iteso.desi.cloud.hw1;

import com.amazonaws.regions.Regions;
import mx.iteso.desi.cloud.keyvalue.KeyValueStoreFactory;

/**
 *
 * @author Álvaro Parres & Mario Contreras
 */
public class Config {

    public static enum DYNAMODBCLIENTTYPE {
        Local, Remote
    }
    
    public static final DYNAMODBCLIENTTYPE DynamoDbClientType = DYNAMODBCLIENTTYPE.Remote;
    
    // The type of key/value store you are using. Initially set to BERKELEY;
    // will be changed to DynamoDB in some phases.
    //public static final KeyValueStoreFactory.STORETYPE storeType = KeyValueStoreFactory.STORETYPE.BERKELEY;
    public static final KeyValueStoreFactory.STORETYPE storeType = KeyValueStoreFactory.STORETYPE.DYNAMODB;
    public static final String pathToDatabase = "C:\\Databases\\BerkeleyDB";

    public static final String accessKeyID = "<useYourKey>";
    public static final String secretAccessKey = "<useYourKey>";

    // Set to your Amazon REGION tu be used
    public static final Regions amazonRegion = Regions.US_EAST_1;

    // Restrict the topics that should be indexed. For example, when this is
    // set to 'X', you should only index topics that start with an X.
    // Set this to "A" for local work, and to "Ar" for cloud tests..
    public static final String filter = "Arab";

    public static final String titleFileName = "C:\\Databases\\TTL\\labels_en.ttl";
    public static final String imageFileName = "C:\\Databases\\TTL\\images_en.ttl";
}

// EOF
