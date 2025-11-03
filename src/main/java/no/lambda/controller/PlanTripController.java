package no.lambda.controller;

import com.github.dockerjava.api.exception.BadRequestException;
import no.lambda.Services.EnturService;
import no.lambda.Validator.TripValidator;
import no.lambda.client.entur.Geocoder.EnturGeocoderClient;
import no.lambda.client.entur.Reverse.EnturReverseClient;
import no.lambda.client.entur.dto.TripPattern;
import no.lambda.client.entur.dto.TripRequestDto;
import no.lambda.client.entur.dto.TripRequestInputDto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlanTripController {

    private final static String tripQuery;

    static {
        try {
            tripQuery = Files.readString(Path.of("src/main/resources/grapql/entur/plan_trip.graphql"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private final EnturService _enturService;

    public PlanTripController() throws Exception {
        this(new EnturService());
    }
    public PlanTripController(EnturService enturService) throws Exception {

        this._enturService = enturService != null ? enturService : new EnturService();
    }

    public ArrayList<EnturGeocoderClient.GeoHit> geoHits(String text) throws Exception{
        return _enturService.getGeoHit(text);
    }

    public ArrayList<EnturReverseClient.RevereseHit> revereseHits(double lat, double lon, int boundaryCircleRadius, int size, String layers) throws Exception {
        return _enturService.getReverseHit(lat, lon, boundaryCircleRadius, size, layers);
    }


    public List<TripPattern> planTrip(TripRequestInputDto request) throws Exception {
        //Validerer input
        List<String> errors = TripValidator.validateInputDto(request);
        if (!errors.isEmpty()){
           throw new BadRequestException(String.valueOf(errors));
        }

        //geocoder fra og til stedene
        var fromFeatures = geoHits(request.getFrom());
        var toFeatures = geoHits(request.getTo());

        if (fromFeatures.isEmpty() || toFeatures.isEmpty()){
            throw new BadRequestException("Locations not found");
        }

        //henter så førte POI/sted
        var fromGeo = fromFeatures.get(0);
        var toGeo = toFeatures.get(0);

        //oppretter en dto som skal mappes med graphQL variablene.

        var enrichedRequestDto = new TripRequestDto(fromGeo.label(),
                fromGeo.latitude(),
                fromGeo.longitude(),
                toGeo.label(),
                toGeo.placeId(),
                toGeo.latitude(),
                toGeo.longitude(),
                5,
                OffsetDateTime.parse(request.getTime()),
                request.isArriveBy()
                );

        //her kan vi validere enrichedRequestDto, sjekke kordinatene osv..

        //mapper input vardier til graphQL variablene
        Map<String, Object> variables = Map.of(
                "fromName", enrichedRequestDto.getFromName(),
                "latitude", enrichedRequestDto.getLatitude(),
                "longitude", enrichedRequestDto.getLongitude(),
                "toName", enrichedRequestDto.getToName(),
                "placeId", enrichedRequestDto.getPlaceId(),
                "toLatitude", enrichedRequestDto.getToLatitude(),
                "toLongitude", enrichedRequestDto.getToLongitude(),
                "tripPatterns", enrichedRequestDto.getTripPatterns(),
                "dateTime", enrichedRequestDto.getDateTime(),
                "arriveBy", enrichedRequestDto.isArriveBy());

        var dto = _enturService.planATrip(variables);

        return dto.data.trip.tripPatterns;
    }
}
