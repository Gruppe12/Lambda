package no.lambda;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.UnauthorizedResponse;
import no.lambda.Storage.adapter.ReiseKlarAdapter;
import no.lambda.Storage.database.MySQLDatabase;
import no.lambda.autentisering.Roller;
import no.lambda.model.Rute;

import java.sql.Connection;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;


import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.validation.ValidationException;


import no.lambda.client.entur.dto.TripPattern;
import no.lambda.controller.PlanTripController;

import static no.lambda.autentisering.Inlogging.getUserId;
import static no.lambda.autentisering.Inlogging.getUserRole;


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
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public", Location.CLASSPATH);
        } ).start();



        // sjekker inlogging
        app.beforeMatched(ctx -> {
            // Dette gjør at Mennesker kan se ui, whoops :P
            if (ctx.routeRoles().isEmpty()){
                return;
            }

            var role = getUserRole(ctx);

//            if (ctx.routeRoles().contains(Roller.OPEN)){
//                return;
//            } else
            if (!ctx.routeRoles().contains(role)) {
                throw new UnauthorizedResponse();
            }
        });

        app.exception(ValidationException.class, (e, ctx) -> {
            ctx.status(400).json(e.getErrors());
        });

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
            _controller.createFavoriteRouteWithoutFavoriteId(rute);
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
      
      
        /*
         Query Parameter
         eksempel url: http://localhost:8080/api/trips?from=Oslo&to=Bergen&time=2025-10-23T19:37:25.123%2B02:00&arriveBy=false
         ADVARSEL!! --- Tiden må være i formatet 2025-10-23T19:37:25.123%2B02:00 og kan ikke være tilbake i tid.
        */

        app.get("/api/trips", ctx -> {

            // leser "from" som frontend har lagt i URL, that's what queryParam is. med queryParmAsString henter og validerer.
            String from = ctx.queryParamAsClass("from", String.class) // String.class sier til Javalin hvilken type den skal bruke

                    // Her sjekker vi brukerens input. Hvis feltet er tomt, lengre enn 60 tegn
                    // eller inneholder tegn som ikke er tillatt, så stopper vi inputen
                    .check( inputFrom -> !inputFrom.isBlank(), "Dette felte kan ikke vare blank!")

                    /* Disse funker omtrent som if. Vi sjekker input, og hvis den matcher
                    det vi forventer er det true, hvis ikke false. blah blah */
                    .check(inputFrom -> inputFrom.length() <= 60, "Allt for lang input")
                    .check(inputFrom -> inputFrom.matches(allowedCharacters), "Ugyldige tegn")
                    // Henter input etter alle sjekker eller sender til exception 400
                    .get();

            String to = ctx.queryParamAsClass("to", String.class)
                    .check( inputTo -> !inputTo.isBlank(), "Dette felte kan ikke vare blank!")
                    .check(inputTo -> inputTo.length() <= 60, "Allt for lang input")
                    .check(inputTo -> inputTo.matches(allowedCharacters), "Ugyldige tegn")
                    .get();


            String time = ctx.queryParamAsClass("time", String.class)
                    // Igjen lager variabel og ser om det er mulig å gjøre det til den forventet tid format
                    .check(inputTime -> {
                        try {
                            OffsetDateTime.parse(inputTime);
                            return true;
                        } catch (Exception error) {
                            return false;
                        }
                    }, "Ugyldig tidsformat")
                    .get();

            // Det finnes sikkert andre mulige sjekker og sånt, you just have to be not me to figure them all out.

            boolean arriveBy = Boolean.parseBoolean(ctx.queryParam("arriveBy"));


            String fromLabel = "";
            double fromLatitude;
            double fromLongitude;

            // Fra her er det så mange mulig sjekk som må gjøres, NOPE.

            // Theres got to be a better way to check for cordinates ;-; ser om det er bare tall og ., hvis ikke kjører
            if (!from.strip().matches("^[0-9 .,]+$")){
                // Finner kordinater ved bruk av enTur autocomplete
                var fromFeatures = _controller.geoHits(from);
                var fromGeoHit = fromFeatures.get(0);

                fromLabel = fromGeoHit.label();
                fromLatitude = fromGeoHit.latitude();
                fromLongitude = fromGeoHit.longitude();
            // Hvis ja kjører inni løkken her til å sette opp for koordinater.
            } else {

                // lager array som vi kan sette in splita settning
                String[] splitFrom = from.split(",");

                if (splitFrom.length != 2) {

                    // Kaster error 400 manuelt
                    throw new BadRequestResponse("Ukjent input, dette er ikke kordinater eller sted.");
                }

                // for kordinatene til å vare double for jeg tror entur accepterer ikke string. oh no
                fromLatitude = Double.parseDouble(splitFrom[0].strip());
                fromLongitude = Double.parseDouble(splitFrom[1].strip());

            }

            String toLabel = "";
            String toPlaceId = "";
            double toLatitude;
            double toLongitude;

            if (!to.strip().matches("^[0-9 .,]+$")) {
                var toFeatures = _controller.geoHits(to);
                var toGeoHit = toFeatures.get(0);

                toLabel = toGeoHit.label();
                toPlaceId = toGeoHit.placeId();
                toLatitude = toGeoHit.latitude();
                toLongitude = toGeoHit.longitude();
            } else {

                String[] splitTo = to.split(",");
                if (splitTo.length != 2) {
                    throw new BadRequestResponse("Ukjent input, dette er ikke kordinater eller sted.");
                }

                toLatitude = Double.parseDouble(splitTo[0].strip());
                toLongitude = Double.parseDouble(splitTo[1].strip());
            }

            List<TripPattern> trip = _controller.planTrip(
                    fromLabel,
                    fromLatitude,
                    fromLongitude,
                    toLabel,
                    toPlaceId,
                    toLatitude,
                    toLongitude,
                    5,
                    OffsetDateTime.parse(time), arriveBy
            );

            //sender koordinater til
            ArrayList<Double> cords = new ArrayList<>();
            cords.add(fromLatitude);
            cords.add(fromLongitude);
            cords.add(toLatitude);
            cords.add(toLongitude);

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

