package no.lambda.Services;
import  com.fasterxml.jackson.databind.JsonNode;
import kotlin.text.UStringsKt;
import no.lambda.Storage.adapter.ReiseKlarAdapter;
import no.lambda.client.entur.Reverse.EnturReverseClient;
import no.lambda.port.ReiseKlarPort;
import no.lambda.client.entur.Geocoder.EnturGeocoderClient;
import no.lambda.client.entur.GraphQL.EnturGraphQLClient;
import no.lambda.client.entur.dto.TripResponseDto;
import no.lambda.client.entur.GraphQL.EnturGraphQLClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
      
public class EnturService implements IPlanTripService{
    private final String _planTripQuery;
    private final EnturGraphQLClient _graphQLClient;
    private final EnturGeocoderClient _geocoderClient;
    private final EnturReverseClient _reverseClient;

    public EnturService() throws Exception {
        this(
                Files.readString(Path.of("src/main/resources/grapql/entur/plan_trip.graphql")),
                new EnturGraphQLClient(),
                new EnturGeocoderClient(),
                new EnturReverseClient()
                );
    }

    public EnturService(String planTripQuery,EnturGraphQLClient graphQLClient, EnturGeocoderClient geocoderClient, EnturReverseClient reverseClient) throws Exception{
        this._graphQLClient = graphQLClient != null ? graphQLClient : new EnturGraphQLClient();
        this._geocoderClient = geocoderClient != null ? geocoderClient : new EnturGeocoderClient();
        this._reverseClient = reverseClient != null ? reverseClient : new EnturReverseClient();
        this._planTripQuery = planTripQuery != null ? planTripQuery : Files.readString(Path.of("src/main/resources/grapql/entur/plan_trip.graphql"));
    }

    public TripResponseDto planATrip( Map<String, Object> variables) throws Exception{
            TripResponseDto dto = _graphQLClient.execute(_planTripQuery, variables);

            if (dto == null || dto.data == null || dto.data.trip.tripPatterns == null){
                throw new RuntimeException("Error: Trip response is null");
            }
        return dto;
    }

    public ArrayList<EnturGeocoderClient.GeoHit> getGeoHit(String text) throws Exception{
        var geoHits = _geocoderClient.geoCode(text);
        return geoHits;
    }

    public ArrayList<EnturReverseClient.RevereseHit> getReverseHit(double lat, double lon, int boundaryCircleRadius, int size, String layers) throws IOException {
        var reverseHits = _reverseClient.reverse(lat, lon, boundaryCircleRadius, size, layers);
        return  reverseHits;
    }
}

