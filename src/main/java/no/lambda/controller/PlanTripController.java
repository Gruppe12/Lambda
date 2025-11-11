package no.lambda.controller;

import no.lambda.Services.DbService;
import no.lambda.Services.EnturService;
import no.lambda.Services.IPlanTripService;
import no.lambda.Storage.adapter.ReiseKlarAdapter;
import no.lambda.Storage.database.MySQLDatabase;
import no.lambda.client.entur.Geocoder.EnturGeocoderClient;
import no.lambda.client.entur.Reverse.EnturReverseClient;
import no.lambda.client.entur.dto.TripPattern;
import no.lambda.model.Rute;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlanTripController {

    private final static String tripQuery;
    private final static String URL = "jdbc:mysql://itstud.hiof.no:3306/se25_G12";
    private final static String USERNAME = "gruppe12";
    private final static String PASSWORD = "Summer31";

    MySQLDatabase database = new MySQLDatabase(URL, USERNAME, PASSWORD);
    Connection dbConnection = database.startDB();

    ReiseKlarAdapter reiseKlarAdapter = new ReiseKlarAdapter(dbConnection);

    static {
        try {
            tripQuery = Files.readString(Path.of("src/main/resources/grapql/entur/plan_trip.graphql"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private final EnturService _enturService;

    public PlanTripController() throws Exception {
        this(new EnturService()
                );
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


    public List<TripPattern> planTrip(String fromName, double latitude, double longitude, String toName, String placeId, double toLatitude ,double toLongitude, int tripPatterns, OffsetDateTime dateTime, boolean arriveBy) throws Exception {
        Map<String, Object> variables = Map.of(
                "fromName", fromName,
                "latitude", latitude,
                "longitude", longitude,
                "toName", toName,
                "placeId", placeId,
                "toLatitude", toLatitude,
                "toLongitude", toLongitude,
                "tripPatterns", tripPatterns,
                "dateTime", dateTime,
                "arriveBy", arriveBy);

        var dto = _enturService.planATrip(variables);

        return dto.data.trip.tripPatterns;
    }

    public void connectToDb() throws Exception {
        DbService.connectToDatabase();
    }


    public void createFavoriteRoute(Rute rute) throws Exception{
        reiseKlarAdapter.createFavoriteRouteWithoutFavoriteId(rute);
    }

    public void createFavoriteRouteWithoutFavoriteId(Rute rute) throws Exception{
        reiseKlarAdapter.createFavoriteRouteWithoutFavoriteId(rute);
    }

    public void createUser(String fornavn, String etternavn){
        reiseKlarAdapter.createUser(fornavn, etternavn);
    }

    public void getFavoriteRoute(int favorittruteId){
        reiseKlarAdapter.getFavoriteRoute(favorittruteId);
    }

    public void getToAndFromBasedOnFavoriteRouteIDAndUserID(int favorittruteId, int brukreId){
        reiseKlarAdapter.getToAndFromBasedOnFavoriteRouteIDAndUserID(favorittruteId, brukreId);
    }

    public void getFavoriteRoutesFromUserBasedOnId(int brukerId){
        reiseKlarAdapter.getFavoriteRoutesFromUserBasedOnId(brukerId);
    }

    public void deleteUserBasedOnFavoriteRouteId(int favorittruteId){
        reiseKlarAdapter.deleteFavoriteRouteBasedOnFavoriteRouteId(favorittruteId);
    }
}
