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
package mx.iteso.msc.asn.temperaturecollector;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

/**
 *
 * @author Mario Contreras <marioc@nazul.net>
 */
public class TemperatureCollector
        implements RequestHandler<Temperature, JSONObject> {

    @Override
    public JSONObject handleRequest(Temperature t, Context c) {
        JSONObject json = new JSONObject();
        AmazonDynamoDB db = AmazonDynamoDBClientBuilder.standard()
                .withRegion(Regions.US_EAST_1).build();
        String tableName = "BitacoraLectura";

        c.getLogger().log(("Temperature " + t.getTemperature() + " received from sensor " + t.getSensorId() + "\n"));

        Map<String, AttributeValue> item = new HashMap<>();
        item.put("SensorId", new AttributeValue(t.getSensorId()));
        item.put("Temperature", new AttributeValue(Float.toString(t.getTemperature())));
        db.putItem(tableName, item);

        json.put("result", "Ok");
        
        c.getLogger().log(("Record added\n"));
        return json;
    }
}

// EOF
