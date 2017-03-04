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

/**
 * Simple class to hold and compare geographic coordinates
 */
public class Geocode {

    String name;
    double lat;
    double lon;

    public Geocode(String name, double lat, double lon) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return lat;
    }

    public double getLongitude() {
        return lon;
    }

    /**
     * Haversine distance computation between lat/long coordinates From
     * http://stackoverflow.com/questions/120283/working-with-latitude-longitude-values-in-java
     */
    public double getHaversineDistance(double latTo, double lonTo) {
        double earthRadius = 6371; //km

        double lat1 = Math.toRadians(latTo);
        double lon1 = Math.toRadians(lonTo);
        double lat2 = Math.toRadians(getLatitude());
        double lon2 = Math.toRadians(getLongitude());

        double dLng = (lon2 - lon1);
        double dLat = (lat2 - lat1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double km = earthRadius * c;

        return (km * 1000);
        //return km;
    }

    @Override
    public int hashCode() {
        return name.hashCode() * 163 + (new Double(lat).hashCode() ^ new Double(lon).hashCode());
    }

    @Override
    public String toString() {
        return name + "\t(" + lat + "," + lon + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Geocode)) {
            return false;
        }

        Geocode u = (Geocode) o;
        return name.equals(u.name) && u.lat == lat && u.lon == lon;
    }
}

// EOF
