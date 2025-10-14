package no.lambda.Services;
import no.lambda.client.entur.Geocoder.EnturGeocoderClient;
import no.lambda.client.entur.GraphQL.EnturGraphQLClient;
import no.lambda.client.entur.dto.TripResponseDto;

import java.util.List;
import java.util.Map;


public class EnturService implements IPlanTripService{
    private final EnturGraphQLClient _graphQLClient;
    private final EnturGeocoderClient _geocoderClient;

    public EnturService(EnturGraphQLClient graphQLClient, EnturGeocoderClient geocoderClient) throws Exception{
        this._graphQLClient = graphQLClient;
        _geocoderClient = geocoderClient;
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

