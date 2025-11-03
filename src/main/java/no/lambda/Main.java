package no.lambda;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import no.lambda.Services.EnturService;
import no.lambda.Storage.adapter.ReiseKlarAdapter;
import no.lambda.Storage.database.MySQLDatabase;
import no.lambda.client.entur.Geocoder.*;
import no.lambda.client.entur.GraphQL.EnturGraphQLClient;
import no.lambda.client.entur.Reverse.EnturReverseClient;
import no.lambda.client.entur.dto.TripPattern;

import no.lambda.client.entur.dto.TripRequestInputDto;
import no.lambda.controller.PlanTripController;

import java.sql.Connection;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {


        //dependencies
        var _controller = new PlanTripController();

        var input = new TripRequestInputDto("Høvik Kirke", "Lysaker stasjon", OffsetDateTime.now().toString(), false);
        List<TripPattern> response = _controller.planTrip(input);


        //Bruker ObjectMapper for å prettify utskrift
       ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        for (TripPattern rute : response){
            System.out.println("Rute: ");
            //System.out.print(rute);
            System.out.println(mapper.writeValueAsString(rute));
        }

        //tester Entur reverse
        System.out.println("!tester Entur reverse!");

        var reverHits = _controller.revereseHits(59.899146, 10.578622, 1, 10, "address,locality");
        var firstHit = reverHits.get(0);
        System.out.println(firstHit);

    }
}


