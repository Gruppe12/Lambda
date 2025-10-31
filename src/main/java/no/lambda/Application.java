package no.lambda;
import no.lambda.Services.EnturService;
import no.lambda.Storage.adapter.ReiseKlarAdapter;
import no.lambda.Storage.database.MySQLDatabase;
import no.lambda.client.entur.GraphQL.EnturGraphQLClient;
import no.lambda.client.entur.Reverse.EnturReverseClient;
import no.lambda.model.Bruker;
import no.lambda.model.Rute;
import no.lambda.port.ReiseKlarPort;
import java.sql.Connection;
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
    private final static String URL = "jdbc:mysql://itstud.hiof.no:3306/se25_G12";
    private final static String USERNAME = "gruppe12";
    private final static String PASSWORD = "Summer31";

    public static void main(String[] args) throws Exception {

        // Konfigurerer database
        MySQLDatabase database = new MySQLDatabase(URL, USERNAME, PASSWORD);
        Connection dbConnection = database.startDB();

        // Konfigurerer klasse (for database-spørringer)
        var reiseKlarAdapter = new ReiseKlarAdapter(dbConnection);

        var _controller = new PlanTripController();
         
        //Etter en merge conflict vurderer jeg å fjerne denne, men lar den stå for nå og kommenterer ut.
        //EnturGraphQLClient client = new EnturGraphQLClient();

        // Oppretter EnturService
        // EnturService enturService = new EnturService(client, reiseKlar);

        // Demonstrasjon av opprettelser av nye rader i databasen.
        Rute eksempelRute1 = new Rute(2, 1, 60.0, 120.0, 40.0, 80.0, 3);
        Rute eksempelRute2 = new Rute(3, 2, 35.0, 41.0, 57.0, 55.0, 5);
        // reiseKlar.createFavoriteRoute(eksempelRute1);
        // reiseKlar.createFavoriteRoute(eksempelRute2);
        // reiseKlar.createUser("John", "Doe");

        // Demonstrasjon av henting av data fra rader i databasen.
        System.out.println("Henter favorittrute:\n" + reiseKlarAdapter.getFavoriteRoute(1) + "\n");
        System.out.println("Henter en liste av fra og til verdier basert på favorittrute_id og bruker_id:\n" + reiseKlarAdapter.getToAndFromBasedOnFavoriteRouteIDAndUserID(1, 1) + "\n");
        //1. fromLongitude 2. fromLatitude 3. toLongitude 4. toLatitude
        System.out.println("Henter en liste av favorittrutekoordinater basert på bruker_id:\n" + reiseKlarAdapter.getFavoriteRoutesFromUserBasedOnId(1) + "\n");
        



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

