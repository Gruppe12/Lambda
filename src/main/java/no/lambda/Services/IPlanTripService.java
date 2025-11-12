package no.lambda.Services;

import no.lambda.client.entur.Geocoder.EnturGeocoderClient;
import no.lambda.client.entur.Reverse.EnturReverseClient;
import no.lambda.client.entur.dto.TripResponseDto;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface IPlanTripService {
    TripResponseDto planATrip(Map<String, Object> variables) throws Exception;
    ArrayList<EnturGeocoderClient.GeoHit> getGeoHit(String text) throws Exception;
    ArrayList<EnturReverseClient.RevereseHit> getReverseHit(double lat, double lon, int boundaryCircleRadius, int size, String layers) throws Exception;

}


