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

import java.util.Set;
import java.util.Iterator;
import java.util.HashSet;
import mx.iteso.desi.cloud.keyvalue.KeyValueStoreFactory;
import mx.iteso.desi.cloud.keyvalue.IKeyValueStorage;
import mx.iteso.desi.cloud.keyvalue.PorterStemmer;

/**
 *
 * @author Álvaro Parres & Mario Contreras
 */
public class QueryImages {

    IKeyValueStorage imageStore;
    IKeyValueStorage titleStore;

    public QueryImages(IKeyValueStorage imageStore, IKeyValueStorage titleStore) {
        this.imageStore = imageStore;
        this.titleStore = titleStore;
    }

    public Set<String> query(String word) {
        HashSet<String> retval = new HashSet<>();
        String term = PorterStemmer.stem(word.toLowerCase());
        HashSet<String> terms = (HashSet<String>) titleStore.get(term);

        for (String t : terms) {
            Set<String> images = imageStore.get(t);
            for (String image : images) {
                retval.add(image);
            }
        }
        return retval;
    }

    public void close() {
        imageStore.close();
        titleStore.close();
    }

    public static void main(String args[]) {
        System.out.println("*** Alumno: Mario Contreras (Exp: 705080)");

        IKeyValueStorage imageStore;
        IKeyValueStorage titleStore;

        try {
            imageStore = KeyValueStoreFactory.getNewKeyValueStore(Config.storeType, "images");
            titleStore = KeyValueStoreFactory.getNewKeyValueStore(Config.storeType, "terms");

            QueryImages myQuery = new QueryImages(imageStore, titleStore);

            for (int i = 0; i < args.length; i++) {
                System.out.println(args[i] + ":");
                Set<String> result = myQuery.query(args[i]);
                Iterator<String> iter = result.iterator();
                while (iter.hasNext()) {
                    System.out.println("  - " + iter.next());
                }
            }

            myQuery.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to complete the indexing pass -- exiting");
        }
    }
}

// EOF
