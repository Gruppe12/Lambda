package no.lambda;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.util.NaiveRateLimit;
import no.lambda.Storage.adapter.ReiseKlarAdapter;
import no.lambda.Storage.database.MySQLDatabase;
import no.lambda.autentisering.Roller;
import no.lambda.model.Rute;

import java.sql.Connection;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


import io.javalin.Javalin;


import no.lambda.client.entur.dto.TripPattern;
import no.lambda.controller.PlanTripController;
import no.lambda.presentation.javalin.*;

import static no.lambda.autentisering.Inlogging.getUserId;


public class Application {
    private final static String URL = "jdbc:mysql://itstud.hiof.no:3306/se25_G12";
    private final static String USERNAME = "gruppe12";
    private final static String PASSWORD = "Summer31";

    // Tillat symboler og karakteerer i bruk for text inputs fra bruker.
    private final static String allowedCharacters = "^[A-Za-zØøÅåÆæ0-9 .,&\\-]+$";

    public static void main(String[] args) throws Exception {

        // Logikk for oppstart av databasen vår

        // Kommenterte ut denne for å teste frontend fordi jeg fikk en error, kunne ikke koble til DB
        connectToDatabase();

        // Logikken for oppstart av nettside i Javalin
        startWebsite();


    }

    public static void connectToDatabase() throws Exception{
        // Konfigurerer database
        MySQLDatabase database = new MySQLDatabase(URL, USERNAME, PASSWORD);
        Connection dbConnection = database.startDB();

        // Konfigurerer klasse (for database-spørringer)
        var reiseKlarAdapter = new ReiseKlarAdapter(dbConnection);

        //Etter en merge conflict vurderer jeg å fjerne denne, men lar den stå for nå og kommenterer ut.
        //EnturGraphQLClient client = new EnturGraphQLClient();

        // Oppretter EnturService
        // EnturService enturService = new EnturService(client, reiseKlar);

        // Demonstrasjon av opprettelser av nye rader i databasen.
        Rute eksempelRute1 = new Rute( 1, 60.0, 120.0, 40.0, 80.0, 3);
        Rute eksempelRute2 = new Rute( 2, 35.0, 41.0, 57.0, 55.0, 5);
        // reiseKlar.createFavoriteRoute(eksempelRute1);
        // reiseKlar.createFavoriteRoute(eksempelRute2);
        // reiseKlar.createUser("John", "Doe");

        // Demonstrasjon av henting av data fra rader i databasen.
        //System.out.println("Henter favorittrute:\n" + reiseKlarAdapter.getFavoriteRoute(1) + "\n");
        //System.out.println("Henter en liste av fra og til verdier basert på favorittrute_id og bruker_id:\n" + reiseKlarAdapter.getToAndFromBasedOnFavoriteRouteIDAndUserID(1, 1) + "\n");
        //1. fromLongitude 2. fromLatitude 3. toLongitude 4. toLatitude
        //System.out.println("Henter en liste av favorittrutekoordinater basert på bruker_id:\n" + reiseKlarAdapter.getFavoriteRoutesFromUserBasedOnId(1) + "\n");
    }


    // Logikken for oppstart av nettsiden
    public static void startWebsite() throws Exception{

        // Lager kontroller for SOMETHING
        var _controller = new PlanTripController();

        // Makes the app object based on the HTML/CSS/JS in the public folder
        // We connect it a port, and start hosting it.
        Javalin app = JavalinServer.create().start(8080);

        ServerConfig.registerCommon(app);

        // ---- API-er ----

        AutocompleteAPI.configure(app, _controller);

        TripsAPI.configure(app, _controller);

        // -- With logg-in

        FavoritesApi.configure(app, _controller);
    }
}

