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

import java.net.UnknownHostException;
import mx.iteso.desi.cloud.hw1.Config;

public class KeyValueStoreFactory {
	public static enum STORETYPE {MEM, BERKELEY, DYNAMODB};
	
	public static STORETYPE DEFAULT_TYPE = Config.storeType;

	/**
	 * Create a new key value storage object
	 * 
	 * @param typ Type of the server
	 * @param dbName Name of the database / table
	 * @return
	 * @throws UnknownHostException
	 */
	public static IKeyValueStorage getNewKeyValueStore(STORETYPE typ, 
			String dbName) throws UnknownHostException {
		switch (typ) {
                    case MEM:
			return new MemStorage();
                    case BERKELEY:
			return new BdbStorage(dbName, false);
                    case DYNAMODB:
			return new DynamoDBStorage(dbName);
                    default:
			return null;
		}
	}

	public static IKeyValueStorage getNewKeyValueStore(String dbName) throws UnknownHostException {
                return KeyValueStoreFactory.getNewKeyValueStore(DEFAULT_TYPE, dbName);
        }        
}

// EOF
