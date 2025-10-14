package no.lambda.Services;
import  com.fasterxml.jackson.databind.JsonNode;
import kotlin.text.UStringsKt;
import no.lambda.client.entur.EnturGraphQLClient;
import no.lambda.port.ReiseKlarPort;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.Map;


public class EnturService {
    private final EnturGraphQLClient client;
    private final String searchStopQuery;
    private final String planTripQuery;
    private final ReiseKlarPort adapter;

    public EnturService(EnturGraphQLClient client, ReiseKlarPort adapter) throws Exception{
        this.client = client;
        this.searchStopQuery = Files.readString(Path.of("src/main/resources/graphql/entur/stop_search.graphql"));
        this.planTripQuery = Files.readString(Path.of("src/main/resources/graphql/entur/plan_trip.graphql"));
        this.adapter = adapter;
    }
    //eksempel på en metode som søker etter stopp
    public JsonNode searchStop(String id) throws Exception{
        return client.execute(searchStopQuery, Map.of("id", id));

    }
    public JsonNode planATrip(String fromName, String toName, int tripPatterns, Date dateTime, boolean arriveBy) throws Exception{
        return client.execute(planTripQuery, Map.of("fromName", fromName, "toName", toName, "tripPatterns", tripPatterns, "dateTime", dateTime, "arriveBy", arriveBy));
    }
}

