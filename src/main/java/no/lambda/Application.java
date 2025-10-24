package no.lambda;
import io.javalin.http.staticfiles.Location;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.javalin.Javalin;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import no.lambda.Services.EnturService;
import no.lambda.client.entur.Geocoder.EnturGeocoderClient;
import no.lambda.client.entur.GraphQL.EnturGraphQLClient;
import no.lambda.client.entur.dto.TripPattern;
import no.lambda.controller.PlanTripController;


public class Application {
    public static void main(String[] args) throws Exception {

        //dependencies
        var _client = new EnturGraphQLClient();
        var _geocoder = new EnturGeocoderClient();
        var service = new EnturService(_client, _geocoder);
        var _controller = new PlanTripController(service);


        // Makes the app object based on the HTML/CSS/JS in the public folder
        // We connect it a port, and start hosting it.
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public", Location.CLASSPATH);
        }).start();


        // Query Parameters
        // example url: http://localhost:8080/api/trips?from=Oslo&to=Bergen&time=2025-10-23T19:37:25.123%2B02:00&arriveBy=false
        // ADVARSEL!! --- Tiden må være i formatet 2025-10-23T19:37:25.123%2B02:00 og kan ikke være tilbake i tid.

        app.get("/api/trips", ctx -> {
            String from = ctx.queryParam("from");
            String to = ctx.queryParam("to");
            String time = ctx.queryParam("time");
            boolean arriveBy = Boolean.parseBoolean(ctx.queryParam("arriveBy"));

            var fromFeatures = _controller.geoHits(from);
            var toFeatures = _controller.geoHits(to);

            var fromGeoHit = fromFeatures.get(0);
            var toGeoHit = toFeatures.get(0);


            List<TripPattern> response = _controller.planTrip(
                    fromGeoHit.label(),
                    fromGeoHit.latitude(),
                    fromGeoHit.longitude(),
                    toGeoHit.label(),
                    toGeoHit.placeId(),
                    toGeoHit.latitude(),
                    toGeoHit.longitude(),
                    5,
                    OffsetDateTime.parse(time), arriveBy
            );

            ctx.json(response);


        });

    }
}
