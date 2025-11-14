package no.lambda.presentation.javalin;

import io.javalin.Javalin;
import no.lambda.Storage.adapter.ReiseKlarAdapter;
import no.lambda.autentisering.Roller;
import no.lambda.controller.PlanTripController;
import no.lambda.model.Rute;
import no.lambda.Validator.CoordinatesValidator;


import java.util.ArrayList;

import static no.lambda.autentisering.Inlogging.getUserId;


public class FavoritesApi {


    public static void configure(Javalin app, PlanTripController controller) {
        configureCheckIfFavorite(app, controller);
        configureRemoveFavorite(app, controller);
        configureAddToFavorite(app, controller);
        configureGetFavorites(app, controller);
    }


    /**
     * Checks if a user has already saved coordinates.
     *
     * Query parameters:
     * fromCoords   "lat,lon" (must contain exactly two numeric parts)
     * toCoords     "lat,lon"
     *
     * Example request:
     * GET http://localhost:8080/api/checkIfFavorite?fromCoords=59.28101884283402,11.11584417329596&toCoords=59.28281465078122,11.108229734377803
     * Header:
     *   "Bruker-id": "123"
     *
     * Example response (json):
     * 0   no match
     * or returns the id of the saved favorite
     * 5 route is already saved
     *
     *
     * Access: must be logged in (Roller.LOGGED_IN)
     *
     * @param app Javalin instance
     * @param controller controller used to check stored favorite routes
     */
    private static void configureCheckIfFavorite(Javalin app, PlanTripController controller) {
        app.get("/api/checkIfFavorite", ctx -> {
            var userId = getUserId(ctx);

            String fromCoords = ctx.queryParamAsClass("fromCoords", String.class)
                    .check(CoordinatesValidator::notBlank, "Dette felte kan ikke vare blank!")
                    .check(CoordinatesValidator::maxLength, "Allt for lang input")
                    .check(CoordinatesValidator::allowedCharacters, "Ugyldige tegn")
                    .check(CoordinatesValidator::hasTwoParts, "Mindre eller flere kordinat blokker")
                    .get();

            String toCoords = ctx.queryParamAsClass("toCoords", String.class)
                    .check(CoordinatesValidator::notBlank, "Dette felte kan ikke vare blank!")
                    .check(CoordinatesValidator::maxLength, "Allt for lang input")
                    .check(CoordinatesValidator::allowedCharacters, "Ugyldige tegn")
                    .check(CoordinatesValidator::hasTwoParts, "Mindre eller flere kordinat blokker")
                    .get();

            // Splitter inputene som vi for fra front end til 2
            String[] splitFromCoords = fromCoords.split(",");
            double fromLat = Double.parseDouble(splitFromCoords[0].strip());
            double fromLon = Double.parseDouble(splitFromCoords[1].strip());

            String[] splitToCoords = toCoords.split(",");
            double toLat = Double.parseDouble(splitToCoords[0].strip());
            double toLon = Double.parseDouble(splitToCoords[1].strip());

            int exists = controller.checkIfFavoriteRouteAlreadyExists(userId, fromLon, fromLat, toLon, toLat);

            ctx.json(exists);
        }, Roller.LOGGED_IN);
    }

    /**
     * Removes a saved favorite route based on its favorite ID.
     *
     * Query parameters:
     * favoritId   number of the saved id from database.
     *
     * Example request:
     * GET http://localhost:8080/api/removeFavorite?favoritId=42
     * Header:
     *   "Bruker-id": "12"
     *
     * Example response:
     * (no response body on success, status 200)
     *
     * Access: requires login (Roller.LOGGED_IN)
     *
     * @param app Javalin instance
     * @param controller controller used to remove stored favorite routes
     */
    private static void configureRemoveFavorite(Javalin app, PlanTripController controller) {
        app.get("/api/removeFavorite", ctx -> {
            var userId = getUserId(ctx);

            Integer favoritId = ctx.queryParamAsClass("favoritId", Integer.class)
                    .check(inputTo -> inputTo.describeConstable().isPresent(), "Dette felte kan ikke vare blank!")
                    .get();

            controller.deleteUserBasedOnFavoriteRouteId(favoritId);
        }, Roller.LOGGED_IN);
    }

    /**
     * Saves coordinates provided by user/frontend
     *
     * Query parameters:
     * fromCoords   coordinates "lat,lon" (origin)
     * toCoords     coordinates "lat,lon" (destination)
     *
     * Example request:
     * GET http://localhost:8080/api/addToFavorite?fromCoords=59.918,10.752&toCoords=60.392,5.324
     * Header:
     *   Bruker-id: 123
     *
     * Example response (json):
     * 17 (this is the generated id provided by database)
     *
     * Access: requires login (Roller.LOGGED_IN)
     *
     * @param app Javalin instance
     * @param controller controller used to create and store the favorite route
     */
    private static void configureAddToFavorite(Javalin app, PlanTripController controller) {
        app.get("/api/addToFavorite", ctx -> {

            var userId = getUserId(ctx);

            String fromCoords = ctx.queryParamAsClass("fromCoords", String.class)
                    .check(CoordinatesValidator::notBlank, "Dette felte kan ikke vare blank!")
                    .check(CoordinatesValidator::maxLength, "Allt for lang input")
                    .check(CoordinatesValidator::allowedCharacters, "Ugyldige tegn")
                    .check(CoordinatesValidator::hasTwoParts, "Mindre eller flere kordinat blokker")
                    .get();

            String toCoords = ctx.queryParamAsClass("toCoords", String.class)
                    .check(CoordinatesValidator::notBlank, "Dette felte kan ikke vare blank!")
                    .check(CoordinatesValidator::maxLength, "Allt for lang input")
                    .check(CoordinatesValidator::allowedCharacters, "Ugyldige tegn")
                    .check(CoordinatesValidator::hasTwoParts, "Mindre eller flere kordinat blokker")
                    .get();

            String[] splitFromCoords = fromCoords.split(",");
            double fromLat = Double.parseDouble(splitFromCoords[0].strip());
            double fromLon = Double.parseDouble(splitFromCoords[1].strip());

            String[] splitToCoords = toCoords.split(",");
            double toLat = Double.parseDouble(splitToCoords[0].strip());
            double toLon = Double.parseDouble(splitToCoords[1].strip());

            Rute rute = new Rute(userId, fromLon, fromLat, toLon, toLat, 1);

            int favId = controller.createFavoriteRouteWithoutFavoriteId(rute);

            ctx.json(favId);
        }, Roller.LOGGED_IN);
    }

    /**
     * Gives all destinations a user saved
     *
     * Example request:
     * GET http://localhost:8080/api/getFavorites
     * Header:
     *   "Bruker-id": "123"
     *
     * Example response (json):
     * [
     *   [
     *     { ... reverseHitsFrom ... },   // name and address for origin
     *     { ... reverseHitsTo ... },     // name and address for destination
     *     17                             // favoriteId
     *   ],
     *   [
     *     { ... reverseHitsFrom ... },
     *     { ... reverseHitsTo ... },
     *     42
     *   ]
     * ]
     *
     * Response structure:
     * [
     *   [
     *     reverseHitsFrom,    // result of reverse geocoding (lat,lon -> name/address)
     *     reverseHitsTo,      // same for destination
     *     favoriteId          // ID of the saved favorite route
     *   ],
     *   ...
     * ]
     *
     * Access: requires login (Roller.LOGGED_IN)
     *
     * @param app Javalin instance
     * @param controller controller used to fetch stored favorite routes
     */
    private static void configureGetFavorites(Javalin app, PlanTripController controller) {
        app.get("/api/getFavorites", ctx -> {

            var userId = getUserId(ctx);

            ArrayList<ArrayList<Double>> favoriteRoute = controller.getFavoriteRoutesFromUserBasedOnId(userId);

            ArrayList<Object> userFavorits = new ArrayList<>();

            for (ArrayList<Double> coordinates : favoriteRoute) {
                ArrayList<Object> userFavorit = new ArrayList<>();

                System.out.println("List in for loop: " + coordinates);
                var fromLon = coordinates.get(0);
                var fromLat = coordinates.get(1);
                var toLon = coordinates.get(2);
                var toLat = coordinates.get(3);
                var favoriteId = coordinates.get(4);

                var reversHitsFrom = controller.revereseHits(fromLat, fromLon, 1, 1, "venue,address,locality");
                var reversHitsTo = controller.revereseHits(toLat, toLon, 1, 1, "venue,address,locality");

                userFavorit.add(reversHitsFrom);
                userFavorit.add(reversHitsTo);
                userFavorit.add(favoriteId);

                userFavorits.add(userFavorit);
            }
            ctx.json(userFavorits);
        }, Roller.LOGGED_IN);
    }
}
