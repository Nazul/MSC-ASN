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

import mx.iteso.desi.cloud.Geocode;

/**
 *
 * @author Mario Contreras <marioc@nazul.net>
 */
public class Config {

    // IATA Codes
    public static final Geocode PHL = new Geocode("PHL", 39.88, -75.25);
    public static final Geocode HOU = new Geocode("HOU", 29.97, -95.35);
    public static final Geocode SEA = new Geocode("SEA", 47.45, -122.30);
    public static final Geocode GDL = new Geocode("GDL", 20.67, -103.40);
    public static final Geocode MTY = new Geocode("MTY", 25.65, -100.44);

    public static void main(String[] args) {
        double lat = 20.675;
        double lng = -103.35888888888888;

        System.out.println(GDL);
        System.out.println(MTY);
        System.out.println(GDL.getHaversineDistance(MTY.getLatitude(), MTY.getLongitude()));
        System.out.println(GDL.getHaversineDistance(lat, lng));
        if (Config.PHL.getHaversineDistance(lat, lng) < 5_000
                || Config.HOU.getHaversineDistance(lat, lng) < 5_000
                || Config.SEA.getHaversineDistance(lat, lng) < 5_000
                || Config.GDL.getHaversineDistance(lat, lng) < 5_000
                || Config.MTY.getHaversineDistance(lat, lng) < 5_000) {
            System.out.println("Cerca");
        }
    }
}

// EOF
