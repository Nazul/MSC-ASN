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
package mx.iteso.desi.cloud.hw2;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.io.*;
import org.apache.log4j.Logger;
import mx.iteso.desi.cloud.GeocodeWritable;

public class GeocodeReducer extends Reducer<Text, GeocodeWritable, Text, Text> {

    private final Logger logger = Logger.getLogger(GeocodeReducer.class);

    @Override
    protected void reduce(Text key, Iterable<GeocodeWritable> values, Context context)
            throws IOException, InterruptedException {
        HashMap<Text, GeocodeWritable> output = new HashMap<>();

        GeocodeWritable newValue = new GeocodeWritable("", 0.0, 0.0);
        for (GeocodeWritable value : values) {
            if (newValue.getName().toString().equals("")) {
                newValue.set(value.getName(), new DoubleWritable(newValue.getLatitude()), new DoubleWritable(newValue.getLongitude()));
            }
            if (newValue.getLatitude() == 0.0) {
                newValue.set(newValue.getName(), new DoubleWritable(value.getLatitude()), new DoubleWritable(value.getLongitude()));
            }
            if (newValue.getLatitude() == 0.0) {
                newValue.set(newValue.getName(), new DoubleWritable(value.getLatitude()), new DoubleWritable(value.getLongitude()));
            }
            output.put(key, newValue);
        }
        output.entrySet().forEach((Map.Entry<Text, GeocodeWritable> entry) -> {
            try {
                double lat = entry.getValue().getLatitude();
                double lng = entry.getValue().getLongitude();
                if ((Config.PHL.getHaversineDistance(lat, lng) < 5_000.0
                        || Config.HOU.getHaversineDistance(lat, lng) < 5_000.0
                        || Config.SEA.getHaversineDistance(lat, lng) < 5_000.0
                        || Config.GDL.getHaversineDistance(lat, lng) < 5_000.0
                        || Config.MTY.getHaversineDistance(lat, lng) < 5_000.0)
                        && !entry.getValue().getName().toString().isEmpty()) {
                    context.write(entry.getKey(), new Text(entry.getValue().toString()));
                }
            } catch (IOException | InterruptedException ex) {
                logger.error(ex.getMessage());
            }
        });
    }
}

// EOF
