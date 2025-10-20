package no.lambda.controller;

import no.lambda.Services.EnturService;
import no.lambda.Services.IPlanTripService;
import no.lambda.client.entur.Geocoder.EnturGeocoderClient;
import no.lambda.client.entur.dto.TripPattern;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

public class PlanTripController {

    private final static String tripQuery;

    static {
        try {
            tripQuery = Files.readString(Path.of("src/main/resources/grapql/entur/plan_trip.graphql"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private final IPlanTripService _enturService;


    public PlanTripController(IPlanTripService enturService){
        _enturService = enturService;
    }


    public List<EnturGeocoderClient.GeoHit> geoHits(String text) throws Exception{
        return _enturService.getGeoHit(text);
    }


    public List<TripPattern> planTrip(String fromName, double latitude, double longitude, String toName, String placeId, double toLatitude ,double toLongitude, int tripPatterns, OffsetDateTime dateTime, boolean arriveBy) throws Exception {
        Map<String, Object> variables = Map.of(
                "fromName", fromName,
                "latitude", latitude,
                "longitude", longitude,
                "toName", toName,
                "placeId", placeId,
                "toLatitude", toLatitude,
                "toLongitude", toLongitude,
                "tripPatterns", tripPatterns,
                "dateTime", dateTime,
                "arriveBy", arriveBy);

        var dto = _enturService.planATrip(tripQuery, variables);

        return dto.data.trip.tripPatterns;
    }
}
