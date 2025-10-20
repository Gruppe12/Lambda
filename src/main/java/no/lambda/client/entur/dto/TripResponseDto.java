package no.lambda.client.entur.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TripResponseDto {
    public Data data;

}
