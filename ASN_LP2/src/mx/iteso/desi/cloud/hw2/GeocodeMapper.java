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
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.io.*;
import mx.iteso.desi.cloud.GeocodeWritable;
import mx.iteso.desi.cloud.Triple;

public class GeocodeMapper extends Mapper<LongWritable, Text, Text, GeocodeWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        Triple triple;

        // Ignore errors
        if (value.toString().contains("BAD URI")) {
            return;
        }

        try {
            triple = new Triple(value.toString());

            // geo_coordinates_en.ttl - Point
            if (triple.getPredicate().equals("http://www.georss.org/georss/point")) {
                String obj = triple.getObject();
                String[] vals = obj.split(" ");
                double lat = Double.parseDouble(vals[0]);
                double lng = Double.parseDouble(vals[1]);
                context.write(new Text(triple.getSubject()), new GeocodeWritable("", lat, lng));
                // images_en.ttl - Depiction
            } else if (triple.getPredicate().equals("http://xmlns.com/foaf/0.1/depiction")) {
                context.write(new Text(triple.getSubject()), new GeocodeWritable(triple.getObject(), 0.0, 0.0));
            }

        } catch (Exception ex) {
            // Parse error... ignore it
        }
    }
}

// EOF
