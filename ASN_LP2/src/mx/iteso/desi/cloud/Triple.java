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
package mx.iteso.desi.cloud;

import java.text.ParseException;

/**
 * Simple class representing an RDF triple <subject, predicate, object>
 * and providing accessors for it
 *
 * @author zives
 * @author ahae
 *
 */
public class Triple {

    String[] contents = new String[3];

    /**
     * Empty triple
     */
    public Triple() {
    }

    /**
     * Pre-initialized triple
     *
     * @param subject
     * @param predicate
     * @param object
     */
    public Triple(String subject, String predicate, String object) {
        contents[0] = subject;
        contents[1] = predicate;
        contents[2] = object;
    }

    /**
     * Creates a triple from a string
     *
     * @param str
     * @throws java.text.ParseException
     */
    public Triple(String str) throws ParseException {
        try {
            int subjLAngle = 0;
            int subjRAngle = str.indexOf('>');
            int predLAngle = str.indexOf('<', subjRAngle + 1);
            int predRAngle = str.indexOf('>', predLAngle + 1);
            int objLAngle = str.indexOf('<', predRAngle + 1);
            int objRAngle = str.indexOf('>', objLAngle + 1);

            if (objLAngle == -1) {
                objLAngle = str.indexOf('\"', predRAngle + 1);
                objRAngle = str.indexOf('\"', objLAngle + 1);
            }

            String subject = str.substring(subjLAngle + 1, subjRAngle);
            String predicate = str.substring(predLAngle + 1, predRAngle);
            String object = str.substring(objLAngle + 1, objRAngle);

            contents[0] = subject;
            contents[1] = predicate;
            contents[2] = object;
        } catch (Exception ex) {
            throw new java.text.ParseException(str, 0);
        }
    }

    /**
     * Fetch by index
     *
     * @param i
     * @return
     */
    public String get(int i) {
        if ((i < 0) || (i >= 3)) {
            throw new RuntimeException("Out of bounds exception");
        }

        return contents[i];
    }

    /**
     * Set content by index
     *
     * @param i
     * @param content
     */
    public void set(int i, String content) {
        if ((i < 0) || (i >= 3)) {
            throw new RuntimeException("Out of bounds exception");
        }

        contents[i] = content;
    }

    /**
     * Return the object of the triple
     *
     * @return
     */
    public String getObject() {
        return contents[2];
    }

    /**
     * Return the predicate of the triple
     *
     * @return
     */
    public String getPredicate() {
        return contents[1];
    }

    /**
     * Return the predicate (relationship) of the triple
     *
     * @return
     */
    public String getRelationship() {
        return contents[1];
    }

    /**
     * Return the subject of the triple
     *
     * @return
     */
    public String getSubject() {
        return contents[0];
    }

    /**
     * Initialize/replace the object
     *
     * @param content
     */
    public void setObject(String content) {
        contents[2] = content;
    }

    /**
     * Initialize/replace the predicate
     *
     * @param content
     */
    public void setPredicate(String content) {
        contents[1] = content;
    }

    /**
     * Initialize/replace the predicate (relationship)
     *
     * @param content
     */
    public void setRelationship(String content) {
        contents[1] = content;
    }

    /**
     * Initialize/replace the subject
     *
     * @param content
     */
    public void setSubject(String content) {
        contents[0] = content;
    }

    /**
     * Accessor to the entire content
     *
     * @return
     */
    public String[] getCollection() {
        return contents;
    }
}

// EOF
