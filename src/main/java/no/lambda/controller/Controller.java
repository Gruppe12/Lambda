package no.lambda.controller;

import no.lambda.Services.EnturService;
import no.lambda.client.entur.dto.TripPattern;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

public class Controller {

    private final static String tripQuery;

    static {
        try {
            tripQuery = Files.readString(Path.of("src/main/resources/grapql/entur/plan_trip.graphql"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private EnturService _enturService;

    public Controller(EnturService enturService){
        _enturService = enturService;
    }


    //Før dette punktet må vi hente coordinates ved
    //bruk av Geocode klienten og deretter bruke det i planTrip metoden
    public List<TripPattern> planTrip(String fromName, double latitude, double longitude, String toName, String placeId, int tripPatterns, OffsetDateTime dateTime, boolean arriveBy) throws Exception {
        Map<String, Object> variables = Map.of(
                "fromName", fromName,
                "latitude", latitude,
                "longitude", longitude,
                "toName", toName,
                "placeId", placeId,
                "tripPatterns", tripPatterns,
                "datetime", dateTime,
                "arriveBy", arriveBy);

        var dto = _enturService.planATrip(tripQuery, variables);

        return dto.data.trip.tripPatterns;

    }
}
