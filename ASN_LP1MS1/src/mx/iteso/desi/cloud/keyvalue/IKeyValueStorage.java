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

import java.util.Set;

public interface IKeyValueStorage {

    /**
     * Get singleton item match
     *
     * @param search
     * @return
     */
    public Set<String> get(String search);

    /**
     * Test if search key has a match
     *
     * @param search
     * @return
     */
    public boolean exists(String search);

    /**
     * Get result set by key prefix
     *
     * @param search
     * @return
     */
    public Set<String> getPrefix(String search);

    /**
     * Add a key/value pair
     *
     * @param keyword
     * @param value
     */
    public void addToSet(String keyword, String value);

    /**
     * Add a set of values to the keyword
     *
     * @param keyword
     * @param values
     */
    public void addToSet(String keyword, Set<String> values);

    /**
     * Add a singleton value for the keyword
     *
     * @param keyword
     * @param value
     */
    public void put(String keyword, String value);

    /**
     * Shut down storage system
     */
    public void close();

    public void sync();

    public boolean isCompressible();

    public boolean supportsMoreThan256Attributes();

    public boolean supportsPrefixes();
}

// EOF
