package no.lambda;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import no.lambda.Services.EnturService;
import no.lambda.client.entur.Geocoder.*;
import no.lambda.client.entur.GraphQL.EnturGraphQLClient;
import no.lambda.client.entur.dto.TripPattern;
import no.lambda.client.entur.dto.TripResponseDto;
import no.lambda.controller.PlanTripController;

import java.time.OffsetDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        var _client = new EnturGraphQLClient();
        var _geocoder = new EnturGeocoderClient();
        EnturService service = new EnturService(_client, _geocoder);
        PlanTripController _controller = new PlanTripController(service);

        var fromFeatures = _geocoder.geoCode("Halden stasjon, Halden");

        var toFeatures = _geocoder.geoCode("Fredrikstad bussterminal, Fredrikstad");

        //int i = 0;

        /*for (var feature : fromFeatures){

            System.out.println(feature );
            i++;
            System.out.println(i);
        }

         */

        var fromGeoHit = fromFeatures.get(0);
        System.out.println(fromGeoHit);

        var toGeoHit = toFeatures.get(0);
        System.out.println(toGeoHit);

        List<TripPattern> response = _controller.planTrip(
                fromGeoHit.label(),
                fromGeoHit.latitude(),
                fromGeoHit.longitude(),
                toGeoHit.label(),
                toGeoHit.placeId(),
                toGeoHit.latitude(),
                toGeoHit.longitude(),
                1,
                OffsetDateTime.parse("2025-10-14T17:42:57.701+02:00"), false );

        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        for (TripPattern rute : response){
            System.out.println("Rute: ");
            //System.out.print(rute);
            System.out.println(mapper.writeValueAsString(rute));
        }



    }
}


