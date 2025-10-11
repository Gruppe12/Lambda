package no.lambda;

import com.fasterxml.jackson.databind.JsonNode;
import no.lambda.Services.EnturService;
import no.lambda.client.entur.Geocoder.*;
import no.lambda.client.entur.GraphQL.EnturGraphQLClient;
import no.lambda.client.entur.dto.TripResponseDto;

import java.time.OffsetDateTime;

public class Main {
    public static void main(String[] args) throws Exception {
        var _client = new EnturGraphQLClient();
        var _geocoder = new EnturGeocoderClient();
        EnturService service = new EnturService(_client );

        var fromGeoCode = _geocoder.geoCode("Bjerkealleen 5A, Skedsmo");
        var toGeoCode = _geocoder.geoCode("Alna, Oslo");


        TripResponseDto response = service.planATrip(
                fromGeoCode.label(),
                fromGeoCode.latitude(),
                fromGeoCode.longitude(),
                toGeoCode.label(),
                toGeoCode.placeId(),
                1,
                OffsetDateTime.parse("2025-10-10T20:56:13.751+02:00"), false );

        System.out.println(response.data.trip.tripPatterns.toString());


    }
}


