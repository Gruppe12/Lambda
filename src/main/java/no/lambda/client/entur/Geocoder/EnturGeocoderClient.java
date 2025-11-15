package no.lambda.client.entur.Geocoder;
import no.lambda.client.entur.Exceptions.EnturGeocoderException;
import okhttp3.*;
import com.fasterxml.jackson.databind.*; // Importerer Jackson for JSON-håndtering

import java.io.IOException;
import java.net.URLEncoder; // For URL-koding av søketekst
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

// Klient for Entur sin Geocoding/Autocomplete-API
public class EnturGeocoderClient {
    // Record for geo data for POI/addresse/Stop. En uforanderlig data-holder.
    public record GeoHit(String label, double latitude, double longitude, String placeId){ }

    private final OkHttpClient httpClient; // HTTP-klient for nettverksforespørsler
    private final ObjectMapper mapper; // Jackson for JSON-mapping
    private final String baseUrl; // Grunn-URL for geocoder-APIet
    private final String clientName; // Klientnavn for Entur-header

    // Standard konstruktør (produksjon)
    public EnturGeocoderClient(){
        this(new OkHttpClient(), new ObjectMapper(),
                "https://api.entur.io/geocoder/v1/autocomplete",
                "LambdaTechAS-SkoleProsjekt_HIOF2025");
    }

    // Full konstruktør (brukes også for testing/tilpasset oppsett)
    public EnturGeocoderClient(OkHttpClient httpClient, ObjectMapper mapper, String baseUrl, String clientName){

        // Initialiserer feltene med gitt verdi eller en standardinstans
        this.httpClient = httpClient != null ?  httpClient: new OkHttpClient();
        this.mapper = mapper != null ? mapper : new ObjectMapper();
        this.baseUrl = baseUrl != null ? baseUrl : "https://api.entur.io/geocoder/v1/autocomplete";
        this.clientName = clientName != null ? clientName : "LambdaTechAS-SkoleProsjekt_HIOF2025";
    }

    // Utfører et geokodingsøk basert på gitt tekst og returnerer en liste over GeoHit-objekter
    public ArrayList<GeoHit> geoCode(String text) throws EnturGeocoderException {
        var hits = new ArrayList<GeoHit>();

        // Bygger URL-en for API-kallet, inkludert søketeksten URLEncoded
        String url = String.format("%s?lang=no&size=10&text=%s", baseUrl, URLEncoder.encode(text, StandardCharsets.UTF_8));

        // Bygger HTTP GET-forespørselen
        var request = new Request.Builder()
                .url(url)
                // Legger til den påkrevde klientnavn-headeren
                .addHeader("ET-Client-Name", "LambdaTechAS-SkoleProsjekt_HIOF2025")
                .build();

        // Utfører API-kallet og sikrer at responsobjektet lukkes
        try (var response = httpClient.newCall(request).execute()){

            if (!response.isSuccessful()) {
                // Kaster unntak ved HTTP-feil
                throw new EnturGeocoderException("Geocoder error: " + response);
            }

            if (response.body() == null){
                throw new EnturGeocoderException("Empty response body from Entur geocoder");
            }
            // Leser hele JSON-responsen inn i et Jackson JsonNode-tre
            JsonNode root = mapper.readTree(response.body().string());

            // Finner "features"-noden som inneholder listen over treff
            JsonNode featuresNode = root.get("features");

            if (featuresNode == null || !featuresNode.isArray() || featuresNode.isEmpty()) {
                throw new EnturGeocoderException("No geocoding results for: " + text);
            }

            // Itererer gjennom alle treffene ('features') i JSON-responsen
            for (JsonNode  node : featuresNode){

                var coordinates = node.get("geometry").get("coordinates"); // Henter koordinatene (longitude, latitude)
                var properties = node.get("properties"); // Henter egenskaper som label og county

                hits.add(
                        new GeoHit(
                                properties.get("label").asText(), // Navn på stedet/adressen
                                coordinates.get(1).asDouble(), // Latitude (Indeks 1)
                                coordinates.get(0).asDouble(), // Longitude (Indeks 0)
                                // Henter ID hvis den finnes, ellers null
                                properties.hasNonNull("id") ? properties.get("id").asText() : null
                        )
                );
            }
            return hits;
        } catch (IOException e) {
            // Håndterer feil under nettverkskommunikasjon eller JSON-parsing
            throw new EnturGeocoderException(e);
        }
    }
}