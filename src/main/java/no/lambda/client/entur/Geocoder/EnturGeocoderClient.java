package no.lambda.client.entur.Geocoder;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.*;
import com.fasterxml.jackson.databind.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class EnturGeocoderClient {
    //Record for geo data for POIN/addresse/Stop
    public record GeoHit(String label, double latitude, double longitude, String placeId){ }

    private final OkHttpClient httpClient = new OkHttpClient();

    private final ObjectMapper mapper = new ObjectMapper();


    //Metode for request til geocode api-et som returner en GeoHit object
    public GeoHit geoCode(String text) throws Exception{
        //Entur geocoder api endepunktet
        var url = "https://api.entur.io/geocoder/v1/autocomplete?lang=no&size=1&text="
                + URLEncoder.encode(text, StandardCharsets.UTF_8);

        //bygger requesten med OKHTTP3
        var request = new Request.Builder()
                .url(url)
                //Entur krever følgende i headers
                .addHeader("ET-Client-Name", "LambdaTechAS-SkoleProsjekt_HIOF2025")
                .build();


        //bygger responsen med OKHTTP3
        try (var response = httpClient.newCall(request).execute()){
            //Kaster en exception dersom request feiler
            if (!response.isSuccessful()) throw new RuntimeException("Geocoder error: " + response);

            //Leser selve responsen
            JsonNode root = mapper.readTree(response.body().string());

            //Leser så "features" som er en Liste som inneholder alle POI
            JsonNode featuresNode = root.get("features");

            if (featuresNode == null || !featuresNode.isArray() || featuresNode.isEmpty()) {
                throw new RuntimeException("No geocoding results for: " + text);
            }

            //leser så første POI
            JsonNode first = featuresNode.get(0);
            //leser "geometry" noden og deretter "coordinates" noden
            var coordinates = first.get("geometry").get("coordinates");
            var properties = first.get("properties");

            //bygger og returnerer så en GeoHit objekt osm inneholder Label/navn til POI, lat og long og stopId
            return new GeoHit(
                    properties.get("label").asText(),
                    coordinates.get(1).asDouble(),
                    coordinates.get(0).asDouble(),
                    properties.hasNonNull("id") ? properties.get("id").asText() : null
            );
        }
    }
}
