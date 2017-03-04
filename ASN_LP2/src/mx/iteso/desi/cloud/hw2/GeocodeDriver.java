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
import mx.iteso.desi.cloud.GeocodeWritable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 *
 * @author Mario Contreras <marioc@nazul.net>
 */
public class GeocodeDriver {

    public static void main(String[] args)
            throws IOException, InterruptedException, ClassNotFoundException {

        if (args.length != 2) {
            System.err.println("Usage: GeocodeDriver <input path> <output path>");
            System.exit(-1);
        }

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Geocode");
        job.setJarByClass(GeocodeDriver.class);
        job.setMapperClass(GeocodeMapper.class);
        job.setReducerClass(GeocodeReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(GeocodeWritable.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}

// EOF
