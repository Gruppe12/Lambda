package no.lambda.Services;
import  com.fasterxml.jackson.databind.JsonNode;
import kotlin.text.UStringsKt;
import no.lambda.port.ReiseKlarPort;
import no.lambda.client.entur.Geocoder.EnturGeocoderClient;
import no.lambda.client.entur.GraphQL.EnturGraphQLClient;
import no.lambda.client.entur.dto.TripResponseDto;
import no.lambda.client.entur.GraphQL.EnturGraphQLClient;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
      
public class EnturService implements IPlanTripService{
    private final String planTripQuery;
    private final ReiseKlarPort adapter;
    private final EnturGraphQLClient _graphQLClient;
    private final EnturGeocoderClient _geocoderClient;

    public EnturService(EnturGraphQLClient graphQLClient, EnturGeocoderClient geocoderClient, ReiseKlarPort adapter) throws Exception{
        this._graphQLClient = graphQLClient;
        this._geocoderClient = geocoderClient;
        this.planTripQuery = Files.readString(Path.of("src/main/resources/grapql/entur/stop_search.graphql"));
        this.adapter = adapter;
    }

    public TripResponseDto planATrip(String query, Map<String, Object> variables) throws Exception{
            TripResponseDto dto = _graphQLClient.execute(query, variables);

            if (dto == null || dto.data == null || dto.data.trip.tripPatterns == null){
                throw new RuntimeException("Error: Trip response is null");
            }
        return dto;
    }

    public List<EnturGeocoderClient.GeoHit> getGeoHit(String text) throws Exception{
        var hits = _geocoderClient.geoCode(text);
        return hits;
    }

}

