package no.lambda.client.entur.Geocoder;
import no.lambda.client.entur.Exceptions.EnturGeocoderException;
import okhttp3.*;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class EnturGeocoderClient {
    //Record for geo data for POI/addresse/Stop
    public record GeoHit(String label,String County ,double latitude, double longitude, String placeId){ }

    private final OkHttpClient httpClient = new OkHttpClient();

    private final ObjectMapper mapper = new ObjectMapper();



    //Metode for request til geocode api-et som returner en GeoHit object
    public ArrayList<GeoHit> geoCode(String text) throws EnturGeocoderException {
        var hits = new ArrayList<GeoHit>();

        //Entur geocoder API endepunktet
        var url = "https://api.entur.io/geocoder/v1/autocomplete?lang=no&size=10&text="
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

            //Leser så "features" - er en Array som inneholder alle POI
            JsonNode featuresNode = root.get("features");

            if (featuresNode == null || !featuresNode.isArray() || featuresNode.isEmpty()) {
                throw new RuntimeException("No geocoding results for: " + text);
            }

            //oppretter en GeoHit objekt for hver feature i hits
            for (JsonNode  node : featuresNode){

                //JsonNode first = featuresNode.get(0);
                //leser "geometry" noden og deretter "coordinates"
                var coordinates = node.get("geometry").get("coordinates");
                var properties = node.get("properties");

                hits.add(
                        new GeoHit(
                                properties.get("label").asText(),
                                properties.get("county").asText(),
                                coordinates.get(1).asDouble(),
                                coordinates.get(0).asDouble(),
                                properties.hasNonNull("id") ? properties.get("id").asText() : null
                        )
                );
            }
            return hits;
        } catch (IOException e) {
            throw new EnturGeocoderException(e);
        }
    }
}
