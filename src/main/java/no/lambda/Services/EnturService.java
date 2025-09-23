package no.lambda.Services;

import  com.fasterxml.jackson.databind.JsonNode;
import no.lambda.client.entur.EnturGraphQLClient;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;


public class EnturService {
    private final EnturGraphQLClient client;
    private final String searchStopQuery;

    public EnturService(EnturGraphQLClient client) throws Exception{
        this.client = client;
        this.searchStopQuery = Files.readString(Path.of("src/main/resources/graphql/entur/stop_search.graphql"));
    }
    //eksempel på en metode som søker etter stopp
    public JsonNode searchStop(String id) throws Exception{
        return client.execute(searchStopQuery, Map.of("id", id));
    }
}
