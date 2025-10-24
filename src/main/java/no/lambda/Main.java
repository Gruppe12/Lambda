package no.lambda;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import no.lambda.Services.EnturService;
import no.lambda.client.entur.Geocoder.*;
import no.lambda.client.entur.GraphQL.EnturGraphQLClient;
import no.lambda.client.entur.dto.TripPattern;

import no.lambda.controller.PlanTripController;

import java.time.OffsetDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {

        //dependencies
        var _client = new EnturGraphQLClient();
        var _geocoder = new EnturGeocoderClient();
        var _service = new EnturService(_client, _geocoder);
        var _controller = new PlanTripController(_service);

        //En liste med POI's til argumentet
        var fromFeatures = _controller.geoHits("Halden stasjon, Halden");
        //En liste med POI's til argumentet
        var toFeatures = _controller.geoHits("Fredrikstad bussterminal, Fredrikstad");

        //Tar første POI - Fra feltet
        var fromGeoHit = fromFeatures.get(0);
        System.out.println(fromGeoHit); // Printer det ut for å se hvilken POI det er

        //Tar første POI - Til feltet
        var toGeoHit = toFeatures.get(0);

        System.out.println(toGeoHit); // Printer det ut for å se hvilken POI det er

        System.out.println("");
        System.out.println("Rute nedenfor er laget med ovenfor POI's: ");
        System.out.println("");

        //metoden _controller.planTrip sender request til GraphQL API-et og returnerer en
        //liste med TripPatterns, altså "Ruter". tripPatterns er satt til 1 i graphql spørringen.
        //så det blir kun 1 "rute/tripPattern"
        List<TripPattern> response = _controller.planTrip(
                fromGeoHit.label(),
                fromGeoHit.latitude(),
                fromGeoHit.longitude(),
                toGeoHit.label(),
                toGeoHit.placeId(),
                toGeoHit.latitude(),
                toGeoHit.longitude(),
                5,
                OffsetDateTime.parse("2025-10-24T21:00:57.701+02:00"), false );

        //Bruker ObjectMapper for å prettify utskrift
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        for (TripPattern rute : response){
            System.out.println("Rute: ");
            //System.out.print(rute);
            System.out.println(mapper.writeValueAsString(rute));
        }



    }
}


