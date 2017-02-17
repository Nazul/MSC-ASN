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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MemStorage extends BasicKeyValueStore {
	Map<String,List<String>> storage = new HashMap<>();

	@Override
	public Set<String> get(String search) {
		Set<String> theSet = new HashSet<String>();
		if (storage.containsKey(search))
			theSet.addAll(storage.get(search));
		
		return theSet;
	}

	@Override
	public boolean exists(String search) {
		return storage.containsKey(search);
	}

	@Override
	public Set<String> getPrefix(String search) {
		Set<String> results = new HashSet<String>();
		
		for (String k: storage.keySet()) {
			if (k.startsWith(search))
				results.addAll(storage.get(k));
		}
		return results;
	}

	@Override
	public void addToSet(String keyword, String value) {
		if (!storage.containsKey(keyword))
			storage.put(keyword, new ArrayList<String>());
		
		storage.get(keyword).add(value);
	}

	@Override
	public void put(String keyword, String value) {
		addToSet(keyword, value);
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sync() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isCompressible() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean supportsMoreThan256Attributes() {
		return true;
	}

	public boolean supportsPrefixes() {
		return true;
	}

}

// EOF
