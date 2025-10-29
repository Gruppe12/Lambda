package no.lambda;
import no.lambda.Services.EnturService;
import no.lambda.Storage.adapter.ReiseKlarAdapter;
import no.lambda.Storage.database.MySQLDatabase;
import no.lambda.client.entur.EnturGraphQLClient;
import no.lambda.model.Bruker;
import no.lambda.model.Rute;
import no.lambda.port.ReiseKlarPort;
import java.sql.Connection;


public class Application {
    private final static String URL = "jdbc:mysql://itstud.hiof.no:3306/se25_G12";
    private final static String USERNAME = "gruppe12";
    private final static String PASSWORD = "Summer31";

    public static void main(String[] args) throws Exception {

        // Konfigurerer database
        MySQLDatabase database = new MySQLDatabase(URL, USERNAME, PASSWORD);
        Connection dbConnection = database.startDB();

        // Konfigurerer klasse (for database-spørringer)
        ReiseKlarPort reiseKlar = new ReiseKlarAdapter(dbConnection);

        EnturGraphQLClient client = new EnturGraphQLClient();

        // Oppretter EnturService
        // EnturService enturService = new EnturService(client, reiseKlar);

        // Demonstrasjon av opprettelser av nye rader i databasen.
        Rute eksempelRute1 = new Rute(2, 1, 60.0, 120.0, 40.0, 80.0, 3);
        Rute eksempelRute2 = new Rute(3, 2, 35.0, 41.0, 57.0, 55.0, 5);
        // reiseKlar.createFavoriteRoute(eksempelRute1);
        // reiseKlar.createFavoriteRoute(eksempelRute2);
        // reiseKlar.createUser("John", "Doe");

        // Demonstrasjon av henting av data fra rader i databasen.
        System.out.println("Henter favorittrute:\n" + reiseKlar.getFavoriteRoute(1) + "\n");
        System.out.println("Henter fra og til verdier basert på favorittrute_id og bruker_id:\n" + reiseKlar.getToAndFromBasedOnFavoriteRouteIDAndUserID(1, 1) + "\n");
        System.out.println("Henter ruteinformasjon basert på bruker_id:\n" + reiseKlar.getFavoriteRoutesFromUserBasedOnId(2) + "\n");
    }
}

