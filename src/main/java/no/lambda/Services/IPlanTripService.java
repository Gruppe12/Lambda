package no.lambda.Services;

import no.lambda.client.entur.Geocoder.EnturGeocoderClient;
import no.lambda.client.entur.dto.TripResponseDto;

import java.util.List;
import java.util.Map;

public interface IPlanTripService {
    TripResponseDto planATrip(String query, Map<String, Object> variables) throws Exception;
    List<EnturGeocoderClient.GeoHit> getGeoHit(String text) throws Exception;
}


