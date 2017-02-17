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
        // TODO: Return the set of URLs that match the given word,
        //       or an empty set if there are no matches
        return new HashSet<String>();
    }

    public void close() {
        // TODO: Close the databases
    }

    public static void main(String args[]) {
        // TODO: Add your own name here
        System.out.println("*** Alumno: Mario Contreras (Exp: 705080)");

        // TODO: get KeyValueStores
        IKeyValueStorage imageStore = null;
        IKeyValueStorage titleStore = null;

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
    }
}

// EOF
