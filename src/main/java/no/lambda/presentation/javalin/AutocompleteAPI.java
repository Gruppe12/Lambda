package no.lambda.presentation.javalin;

import io.javalin.Javalin;
import io.javalin.http.util.NaiveRateLimit;
import no.lambda.controller.PlanTripController;
import no.lambda.Validator.GeoCodeValidator;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;



/**
 * Autocompletes user inputs
 */
public class AutocompleteAPI {

    /**
     *
     * Query parameters:
     * typedIn   text typed inn by user, that will be used to suggest locations
     *
     * Eksample request
     * GET http://localhost:8080/api/autocomplete?typedIn=Osl
     *
     *
     * Example return (Respons type: json)
     * [
     *   ["Oslo", "Oslo"],
     *   ["Os", "Vestland"]
     * ]
     *
     * @param app is the javalin instance
     * @param controller this gets us the controller that was started in application
     */

    public static void configure(Javalin app, PlanTripController controller) {

        app.get("/api/autocomplete", ctx -> {
            // Added this so the server does not get spammed way to much lol.
            NaiveRateLimit.requestPerTimeUnit(ctx, 5, TimeUnit.SECONDS);

            String typedInForAutocomplete = ctx.queryParamAsClass("typedIn", String.class)
                    .check(GeoCodeValidator::notBlank, "Dette felte kan ikke vare blank!")
                    .check(GeoCodeValidator::maxLength, "Allt for lang input")
                    .check(GeoCodeValidator::allowedCharacters, "Ugyldige tegn")
                    .get();

            // enTur API som gjør autocomplete
            var suggestions = controller.geoHits(typedInForAutocomplete);
            ArrayList<ArrayList<String>> response = new ArrayList<>();


            // Gjør så vi sender bare navn til frontend
            for (var suggested : suggestions) {
                ArrayList<String> pair = new ArrayList<>();
                pair.add(suggested.label());
                pair.add(suggested.County());
                response.add(pair);
            }

            ctx.json(response);
        });
    }

}
