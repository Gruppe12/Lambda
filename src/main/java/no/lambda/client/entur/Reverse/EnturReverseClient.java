package no.lambda.client.entur.Reverse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.lambda.client.entur.Exceptions.EnturException;
import no.lambda.client.entur.Exceptions.EnturReverseException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response; // Implisitt brukt via var i try-with-resources

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Locale; // Brukes for å sikre riktig formatering av desimaltall

// Klient for Entur sitt Reverse Geocoding API
public class EnturReverseClient {
    // Record for treff fra reverse geocoding (stedet/adressens navn)
    public record RevereseHit(String name){}

    private OkHttpClient _okHttpClient; // HTTP-klient for kommunikasjon
    private final ObjectMapper _mapper; // Jackson for JSON-håndtering
    private final String _baseUrl; // API-endepunkt for reverse geocoding


    // Standard konstruktør (produksjon)
    public EnturReverseClient(){
        this(new OkHttpClient(), new ObjectMapper(), "https://api.entur.io/geocoder/v1/reverse");
    }

    // Full konstruktør (tillater tilpasning for testing eller spesielle behov)
    public EnturReverseClient(OkHttpClient okHttpClient, ObjectMapper mapper, String baseUrl){
        this._okHttpClient = okHttpClient != null ? okHttpClient : new OkHttpClient();
        this._mapper = mapper != null ?  mapper : new ObjectMapper();
        this._baseUrl = baseUrl != null ? baseUrl : "https://api.entur.io/geocoder/v1/reverse";
    }

    // Utfører reverse geocoding basert på koordinater
    public ArrayList<RevereseHit> reverse(double lat, double lon, int boundaryCircleRadius, int size, String layers) throws EnturReverseException, IOException {
        var reverseHits = new ArrayList<RevereseHit>();

        // Bygger URL for API-kallet
        String url = String.format(
                // Bruker Locale.US for å sikre at desimaler formateres med punkt (.), som er standard for URL-spørringer
                Locale.US ,"%s?point.lat=%.6f&point.lon=%.6f&boundary.circle.radius=%d&size=%d&layers=%s",
                _baseUrl, lat, lon, boundaryCircleRadius, size, URLEncoder.encode(layers, StandardCharsets.UTF_8)
        );

        // Lager HTTP GET-forespørselen
        var request = new Request.Builder()
                .url(url)
                // Entur Reverse Geocoder krever normalt ikke ET-Client-Name header, men det er god praksis å inkludere hvis påkrevd av Entur. (Siden den ikke er her, holdes den utenfor)
                .build();

        // Utfører API-kallet og bruker try-with-resources for automatisk lukking av Response
        try(var response = _okHttpClient.newCall(request).execute()){

            if (!response.isSuccessful()){
                // Kaster feil hvis HTTP-statuskoden indikerer en feil (f.eks. 4xx eller 5xx)
                throw new EnturReverseException("Entur reverse request error: " + response);
            }
            if (response.body() == null){
                throw new EnturReverseException("Empty response body from Entur reverse" );
            }

            // Leser JSON-responsen fra bodyen inn i et Jackson JsonNode-tre
            JsonNode root = _mapper.readTree(response.body().string());

            // Henter "features"-arrayen, som inneholder treffene
            JsonNode featuresNode = root.get("features");

            if (featuresNode == null || !featuresNode.isArray() || featuresNode.isEmpty()){
                // Kaster feil hvis det ikke er noen treff i svaret
                throw new EnturReverseException("No reversing data for: " + lat + " " + lon);
            }

            // Itererer over alle treff ('features')
            for (JsonNode node : featuresNode){
                var properties = node.get("properties"); // Henter egenskaps-objektet

                reverseHits.add(
                        new RevereseHit(
                                // Henter navnet ('label') til det funnede stedet
                                properties.get("label").asText()
                        )
                );
            }
            return reverseHits; // Returnerer listen over funnede stedsnavn
        }
        // IOException fanges implisitt av metodesignaturen for nettverksfeil
    }
}