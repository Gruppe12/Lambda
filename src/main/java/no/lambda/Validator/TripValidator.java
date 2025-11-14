package no.lambda.Validator;

import no.lambda.client.entur.dto.TripRequestDto;
import no.lambda.client.entur.dto.TripRequestInputDto;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class TripValidator {
    public static List<String> validateRequestDto(TripRequestDto request) {
        List<String> errors = new ArrayList<>();

        if (request.getTripPatterns() <= 0)
            errors.add("Trip patterns must be > 0");

        return errors;
    }


    public static List<String> validateInputDto(TripRequestInputDto request) {
        List<String> errors = new ArrayList<>();

        if (request.getFrom() == null || request.getFrom().isBlank())
            errors.add("From location is required");
        if (request.getTo() == null || request.getTo().isBlank())
            errors.add("To location is required");

        if (request.getTime() == null)
            errors.add("Date and time are required");

        //her kan vi legge til flere validation regler


        return errors;
    }

    // Tids validering

    public static boolean validTime(String value) {
        if (value == null) {
            return false;
        }

        try {
            OffsetDateTime.parse(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }




}
