package no.lambda.Services;
import  com.fasterxml.jackson.databind.JsonNode;
import no.lambda.client.entur.GraphQL.EnturGraphQLClient;
import no.lambda.client.entur.Geocoder.EnturGeocoderClient;
import no.lambda.client.entur.dto.TripResponseDto;
import org.w3c.dom.stylesheets.LinkStyle;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.Map;


public class EnturService {
    private final EnturGraphQLClient client;

    public EnturService(EnturGraphQLClient client) throws Exception{
        this.client = client;
    }

    public TripResponseDto planATrip(String query, Map<String, Object> variables) throws Exception{
            TripResponseDto dto = client.execute(query, variables);
            if (dto == null || dto.data == null || dto.data.trip.tripPatterns == null){
                throw new RuntimeException("Error: Trip response is null");
            }

        return dto;
    }
}

