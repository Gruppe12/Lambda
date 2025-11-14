package no.lambda.presentation.javalin.helpers;

import no.lambda.Validator.CoordinatesValidator;
import no.lambda.controller.PlanTripController;

public class InputLocation {

    public static LocationResult buildLocation(String input, boolean isDestination, PlanTripController controller) throws Exception {
        String label = "";
        String placeId = "";
        double latitude;
        double longitude;

        if (!CoordinatesValidator.isCoordinatePair(input.strip())) {

            // Finner kordinater ved bruk av enTur autocomplete
            var features = controller.geoHits(input);
            var geoHit = features.get(0);

            label = geoHit.label();
            latitude = geoHit.latitude();
            longitude = geoHit.longitude();

            if (isDestination){
                placeId = geoHit.placeId();
            }

        } else {  // Hvis ja kjører inni løkken her til å sette opp for koordinater.

            // lager array som vi kan sette in splita settning
            String[] splitFrom = input.split(",");

            // for kordinatene til å vare double for jeg tror entur accepterer ikke string. oh no
            latitude = Double.parseDouble(splitFrom[0].strip());
            longitude = Double.parseDouble(splitFrom[1].strip());

        }

        return new LocationResult(label, placeId, latitude, longitude);
    }
}
