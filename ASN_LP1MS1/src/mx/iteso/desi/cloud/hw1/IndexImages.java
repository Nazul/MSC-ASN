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

import java.io.IOException;
import java.util.HashSet;
import mx.iteso.desi.cloud.keyvalue.IKeyValueStorage;
import mx.iteso.desi.cloud.keyvalue.KeyValueStoreFactory;
import mx.iteso.desi.cloud.keyvalue.ParseTriples;
import mx.iteso.desi.cloud.keyvalue.PorterStemmer;
import mx.iteso.desi.cloud.keyvalue.Triple;

/**
 *
 * @author Álvaro Parres & Mario Contreras
 */
public class IndexImages {

    ParseTriples parser;
    IKeyValueStorage imageStore, titleStore;

    public IndexImages(IKeyValueStorage imageStore, IKeyValueStorage titleStore) {
        this.imageStore = imageStore;
        this.titleStore = titleStore;
    }

    public void run(String imageFileName, String titleFileName) throws IOException {
        // TODO: This method should load all images and titles 
        //       into the two key-value stores.
    }

    public void close() {
        //TODO: close the databases;
    }

    public static void main(String args[]) {
        // TODO: Add your own name here
        System.out.println("*** Alumno: Mario Contreras (Exp: 705080)");
        try {

            IKeyValueStorage imageStore = KeyValueStoreFactory.getNewKeyValueStore(Config.storeType,
                    "images");
            IKeyValueStorage titleStore = KeyValueStoreFactory.getNewKeyValueStore(Config.storeType,
                    "terms");

            IndexImages indexer = new IndexImages(imageStore, titleStore);
            indexer.run(Config.imageFileName, Config.titleFileName);
            System.out.println("Indexing completed");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to complete the indexing pass -- exiting");
        }
    }
}

// EOF
