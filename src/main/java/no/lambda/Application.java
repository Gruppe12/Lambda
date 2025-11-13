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
import no.lambda.presentation.javalin.AutocompleteAPI;
import no.lambda.presentation.javalin.JavalinServer;
import no.lambda.presentation.javalin.ServerConfig;
import no.lambda.presentation.javalin.TripsAPI;

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

        // Konfigurerer database
        MySQLDatabase database = new MySQLDatabase(URL, USERNAME, PASSWORD);
        Connection dbConnection = database.startDB();

        // Konfigurerer klasse (for database-spørringer)
        var reiseKlarAdapter = new ReiseKlarAdapter(dbConnection);


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

        // eksempel: http://localhost:8080/api/checkIfFavorite??fromCoords=59.28101884283402,11.11584417329596&toCoords=59.28281465078122,11.108229734377803{ headers: { "Bruker-id": "123" }
        app.get("/api/checkIfFavorite", ctx -> {
            var userId = getUserId(ctx);

            String fromCoords = ctx.queryParamAsClass("fromCoords", String.class)
                    .check( inputTo -> !inputTo.isBlank(), "Dette felte kan ikke vare blank!")
                    .check(inputTo -> inputTo.length() <= 60, "Allt for lang input")
                    .check(inputTo -> inputTo.matches("^[0-9 .,]+$"), "Ugyldige tegn")
                    .check(inputTo -> inputTo.split(",").length == 2, "Mindre eller flere kordinat blokker")
                    .get();

            String toCoords = ctx.queryParamAsClass("toCoords", String.class)
                    .check( inputTo -> !inputTo.isBlank(), "Dette felte kan ikke vare blank!")
                    .check(inputTo -> inputTo.length() <= 60, "Allt for lang input")
                    .check(inputTo -> inputTo.matches("^[0-9 .,]+$"), "Ugyldige tegn")
                    .check(inputTo -> inputTo.split(",").length == 2, "Mindre eller flere kordinat blokker")
                    .get();

            // Splitter inputene som vi for fra front end til 2
            String[] splitFromCoords = fromCoords.split(",");
            double fromLat =  Double.parseDouble(splitFromCoords[0].strip());
            double fromLon =  Double.parseDouble(splitFromCoords[1].strip());

            String[] splitToCoords = toCoords.split(",");
            double toLat =  Double.parseDouble(splitToCoords[0].strip());
            double toLon =  Double.parseDouble(splitToCoords[1].strip());

            int exists = _controller.checkIfFavoriteRouteAlreadyExists(userId, fromLon, fromLat, toLon, toLat);


            ctx.json(exists);


        }, Roller.LOGGED_IN);

        // eksempel: http://localhost:8080/api/removeFavorite?favoritId=13 { headers: { "Bruker-id": "123" }
        app.get("/api/removeFavorite", ctx -> {
            var userId = getUserId(ctx);

            Integer favoritId = ctx.queryParamAsClass("favoritId", Integer.class)
                    .check( inputTo -> inputTo.describeConstable().isPresent(), "Dette felte kan ikke vare blank!")
                    .get();

            _controller.deleteUserBasedOnFavoriteRouteId(favoritId);
        }, Roller.LOGGED_IN);

        // eksempel: http://localhost:8080/api/addToFavorite?fromCoords=59.28101884283402, 11.11584417329596&toCoords=59.28281465078122, 11.108229734377803 { headers: { "Bruker-id": "123" }
        app.get("/api/addToFavorite", ctx -> {

            // for  brukerId fra role Klassen
            var userId = getUserId(ctx);

            // Kjekker inputs fra frontend
            String fromCoords = ctx.queryParamAsClass("fromCoords", String.class)
                    .check( inputTo -> !inputTo.isBlank(), "Dette felte kan ikke vare blank!")
                    .check(inputTo -> inputTo.length() <= 60, "Allt for lang input")
                    .check(inputTo -> inputTo.matches("^[0-9 .,]+$"), "Ugyldige tegn")
                    .check(inputTo -> inputTo.split(",").length == 2, "Mindre eller flere kordinat blokker")
                    .get();

            String toCoords = ctx.queryParamAsClass("toCoords", String.class)
                    .check( inputTo -> !inputTo.isBlank(), "Dette felte kan ikke vare blank!")
                    .check(inputTo -> inputTo.length() <= 60, "Allt for lang input")
                    .check(inputTo -> inputTo.matches("^[0-9 .,]+$"), "Ugyldige tegn")
                    .check(inputTo -> inputTo.split(",").length == 2, "Mindre eller flere kordinat blokker")
                    .get();


            // Splitter inputene som vi for fra front end til 2
            String[] splitFromCoords = fromCoords.split(",");
            double fromLat =  Double.parseDouble(splitFromCoords[0].strip());
            double fromLon =  Double.parseDouble(splitFromCoords[1].strip());

            String[] splitToCoords = toCoords.split(",");
            double toLat =  Double.parseDouble(splitToCoords[0].strip());
            double toLon =  Double.parseDouble(splitToCoords[1].strip());


            // legger dem inni databasen
            //int bruker_id, double from_longitude, double from_latitude, double to_longitude, double to_latitude, int to_place_id
            Rute rute = new Rute(userId, fromLon, fromLat, toLon, toLat, 1);

            int fav_id = _controller.createFavoriteRouteWithoutFavoriteId(rute);

            ctx.json(fav_id);


        }, Roller.LOGGED_IN);

        // eksempel: http://localhost:8080/api/getFavorites { headers: { "Bruker-id": "123" }
        app.get("/api/getFavorites", ctx -> {

            // for  brukerId fra role classen
            var userId = getUserId(ctx);

            ArrayList<ArrayList<Double>> favoriteRoute = reiseKlarAdapter.getFavoriteRoutesFromUserBasedOnId(userId);

            // lagger liste som skall lagre navn vi får fra entur
            ArrayList<Object> userFavorits = new ArrayList<>();

            // kjører gjennom listen vi fikk fra databasen av brukers favoriter
            // den henter listene en og en og sender de kordinater til å reversere gjennom entur.
            for (ArrayList<Double> coordinates : favoriteRoute) {
                ArrayList<Object> userFavorit = new ArrayList<>();

                System.out.println("List in for loop: " + coordinates);
                var fromLon = coordinates.get(0);
                var fromLat = coordinates.get(1);
                var toLon = coordinates.get(2);
                var toLat = coordinates.get(3);
                var favoriteId = coordinates.get(4);

                var reversHitsFrom = _controller.revereseHits(fromLat, fromLon, 1, 1, "venue,address,locality");
                var reversHitsTo = _controller.revereseHits(toLat, toLon, 1, 1, "venue,address,locality");
                // lagrer de verdiene vi for fra entur
                userFavorit.add(reversHitsFrom);
                userFavorit.add(reversHitsTo);
                userFavorit.add(favoriteId);

                userFavorits.add(userFavorit);
            }
            ctx.json(userFavorits);
        }, Roller.LOGGED_IN);
        // bruker må vare pålogga til å få brukt denne api gjennom frontend
    }
}

