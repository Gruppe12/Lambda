package no.lambda.client.entur.Reverse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.lambda.client.entur.Exceptions.EnturException;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Locale;

public class EnturReverseClient {
    public record RevereseHit(String name){}

    private OkHttpClient _okHttpClient;
    private final ObjectMapper _mapper;
    private final String _baseUrl;


    public EnturReverseClient(){
        this(new OkHttpClient(), new ObjectMapper(), "https://api.entur.io/geocoder/v1/reverse");
    }

    public EnturReverseClient(OkHttpClient okHttpClient, ObjectMapper mapper, String baseUrl){
        this._okHttpClient = okHttpClient != null ? okHttpClient : new OkHttpClient();
        this._mapper = mapper != null ?  mapper : new ObjectMapper();
        this._baseUrl = baseUrl != null ? baseUrl : "https://api.entur.io/geocoder/v1/reverse";
    }

    public ArrayList<RevereseHit> reverse(double lat, double lon, int boundaryCircleRadius, int size, String layers) throws EnturException, IOException {
        var reverseHits = new ArrayList<RevereseHit>();

        String url = String.format(
                Locale.US ,"%s?point.lat=%.6f&point.lon=%.6f&boundary.circle.radius=%d&size=%d&layers=%s",
                _baseUrl, lat, lon, boundaryCircleRadius, size, URLEncoder.encode(layers, StandardCharsets.UTF_8)
        );

        var request = new Request.Builder()
                .url(url)
                .build();

        try(var response = _okHttpClient.newCall(request).execute()){

            if (!response.isSuccessful()){
                throw new EnturException("Entur reverse request error: " + response);
            }
            if (response.body() == null){
                throw new EnturException("Empty response body from Entur reverse" );
            }

            JsonNode root = _mapper.readTree(response.body().string());

            JsonNode featuresNode = root.get("features");

            if (featuresNode == null || !featuresNode.isArray() || featuresNode.isEmpty()){
                throw new EnturException("No reversing data for: " + lat + " " + lon);
            }

            for (JsonNode node : featuresNode){
                var properties = node.get("properties");

                reverseHits.add(
                        new RevereseHit(
                                properties.get("label").asText()
                        )
                );

            }

        }
        return reverseHits;
    }


}
