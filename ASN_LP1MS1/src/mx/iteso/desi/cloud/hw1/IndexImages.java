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
import java.io.File;
import mx.iteso.desi.cloud.keyvalue.IKeyValueStorage;
import mx.iteso.desi.cloud.keyvalue.KeyValueStoreFactory;
import mx.iteso.desi.cloud.keyvalue.ParseTriples;
import mx.iteso.desi.cloud.keyvalue.PorterStemmer;
import mx.iteso.desi.cloud.keyvalue.Triple;

/**
 *
 * @author Álvaro Parres & Mario Contreras (based on Andreas Haeberlen NETS 212
 * work - http://www.cis.upenn.edu/~nets212/)
 */
public class IndexImages {

    ParseTriples parser;
    IKeyValueStorage imageStore, titleStore;

    public IndexImages(IKeyValueStorage imageStore, IKeyValueStorage titleStore) {
        this.imageStore = imageStore;
        this.titleStore = titleStore;
    }

    public void run(String imageFileName, String titleFileName) throws IOException {
        Triple t;

        System.out.println("=== Images ===");
        parser = new ParseTriples(imageFileName);
        while ((t = parser.getNextTriple()) != null) {
            String fullSubject = t.getSubject();
            String subject = new File(t.getSubject()).getName();
            if (subject.toLowerCase().startsWith(Config.filter.toLowerCase()) && t.getPredicate().toLowerCase().equals("http://xmlns.com/foaf/0.1/depiction")) {
                // Add to Image Store
                System.out.println("***ImageStore***");
                System.out.println("Key: " + t.getSubject());
                System.out.println("Value: " + t.getObject());
                imageStore.addToSet(t.getSubject(), t.getObject());
                System.out.println();
            }
        }
        parser.close();

        System.out.println("=== Terms ===");
        parser = new ParseTriples(titleFileName);
        while ((t = parser.getNextTriple()) != null) {
            String fullSubject = t.getSubject();
            String subject = new File(t.getSubject().toLowerCase()).getName();
            if (subject.startsWith(Config.filter.toLowerCase()) && t.getPredicate().toLowerCase().equals("http://www.w3.org/2000/01/rdf-schema#label") && imageStore.exists(t.getSubject())) {
                String[] terms = t.getObject().toLowerCase().split(" ");
                // Add to Term Store
                System.out.println("***TermStore***");
                for (String term : terms) {
                    String newTerm = PorterStemmer.stem(term);
                    if (!newTerm.equals("Invalid term")) {
                        System.out.println("Key: " + newTerm);
                        System.out.println("Value: " + fullSubject);
                        titleStore.addToSet(newTerm, fullSubject);
                        System.out.println();
                    }
                }
                System.out.println();
            }
        }
        parser.close();

        this.close();
    }

    public void close() {
        imageStore.close();
        titleStore.close();
    }

    public static void main(String args[]) {
        System.out.println("*** Alumno: Mario Contreras (Exp: 705080)");
        try {
            IKeyValueStorage imageStore = KeyValueStoreFactory.getNewKeyValueStore(Config.storeType, "images");
            IKeyValueStorage titleStore = KeyValueStoreFactory.getNewKeyValueStore(Config.storeType, "terms");

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
