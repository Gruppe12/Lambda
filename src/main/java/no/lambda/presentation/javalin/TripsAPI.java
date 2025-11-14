package no.lambda.presentation.javalin;

import io.javalin.Javalin;
import no.lambda.Validator.GeoCodeValidator;
import no.lambda.Validator.TripValidator;
import no.lambda.controller.PlanTripController;
import no.lambda.presentation.javalin.helpers.InputLocation;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * plans journy using enTur api between to locations
 */
public class TripsAPI {

    /**
     *
     * Query parameters:
     * from      name or "lat,lon"
     * to        name or "lat,lon"
     * time      eksempel 2025-10-23T19:37:25.123+02:00 må vare nå eller framtidig tid eller blir det ikke noe output.
     * arriveBy  true hvis tiden er ankomsttid, false hvis det er avreisetid
     *
     * Example request:
     * GET http://localhost:8080/api/trips?from=Oslo&to=Bergen&time=2025-10-23T19:37:25.123%2B02:00&arriveBy=false
     *
     * Example response (json):
     * [
     *   {
     *  *   "expectedStartTime": "2025-11-14T07:20:17+01:00",
     *  *   "expectedEndTime":   "2025-11-14T08:40:00+01:00",
     *  *   "duration": 4783,
     *  *   "walkDistance": 521.4,
     *  *   "legs": [
     *  *     {
     *  *       // transport mode, distance, what line.
     *  *     },
     *  *     {
     *  *       // second leg
     *  *     }
     *  *   ]
     *  * }
     *   [ fromLatitude, fromLongitude, toLatitude, toLongitude ]
     * ]
     *
     * @param app Javalin instance
     * @param controller controller used to validate input and plan trips
     */
    public static void configure(Javalin app, PlanTripController controller) {


        app.get("/api/trips", ctx -> {

            // leser "from" som frontend har lagt i URL, that's what queryParam is. med queryParmAsString henter og validerer.
            String from = ctx.queryParamAsClass("from", String.class) // String.class sier til Javalin hvilken type den skal bruke

                    /* Her sjekker vi brukerens input. Hvis feltet er tomt, lengre enn 60 tegn
                    eller inneholder tegn som ikke er tillatt, så stopper vi inputen

                    Disse funker omtrent som if. Vi sjekker input, og hvis den matcher
                    det vi forventer er det true, hvis ikke false. blah blah */
                    .check(GeoCodeValidator::notBlank, "Dette felte kan ikke vare blank!")
                    .check(GeoCodeValidator::maxLength, "Allt for lang input")
                    .check(GeoCodeValidator::allowedCharacters, "Ugyldige tegn")
                    // Henter input etter alle sjekker eller sender til exception 400
                    .get();

            String to = ctx.queryParamAsClass("to", String.class)
                    .check(GeoCodeValidator::notBlank, "Dette felte kan ikke vare blank!")
                    .check(GeoCodeValidator::maxLength, "Allt for lang input")
                    .check(GeoCodeValidator::allowedCharacters, "Ugyldige tegn")
                    .get();

            // Igjen lager variabel og ser om det er mulig å gjøre det til den forventet tid format
            String time = ctx.queryParamAsClass("time", String.class)
                    .check(TripValidator::validTime, "Ugyldig tidsformat")
                    .get();

            // Det finnes sikkert andre mulige sjekker og sånt, you just have to be not me to figure them all out.
            boolean arriveBy = Boolean.parseBoolean(ctx.queryParam("arriveBy"));

            var fromLocation = InputLocation.buildLocation(from, false, controller);
            var toLocation = InputLocation.buildLocation(to, true, controller);


            var trip = controller.planTrip(
                    fromLocation.label(),
                    fromLocation.lat(),
                    fromLocation.lon(),
                    toLocation.label(),
                    toLocation.placeId(),
                    toLocation.lat(),
                    toLocation.lon(),
                    5,
                    OffsetDateTime.parse(time),
                    arriveBy
            );


            //sender koordinater med respons
            ArrayList<Double> cords = new ArrayList<>();
            cords.add(fromLocation.lat());
            cords.add(fromLocation.lon());
            cords.add(toLocation.lat());
            cords.add(toLocation.lon());

            List<List<?>> response = new ArrayList<>();

            response.add(trip);
            response.add(cords);
            ctx.json(response);

            /*

            You know sometimes I wonder when is it to much to make sure the user can't type in stuff you don't want.
            And still they can SQL inject if there was something to inject into.

                  ******              ******
                  ******              ******
                  ******              ******

            ****************************************
            ****************************************


            */

        });
    }
}
