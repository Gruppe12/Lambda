package no.lambda;
import no.lambda.Services.EnturService;
import no.lambda.Storage.adapter.ReiseKlarAdapter;
import no.lambda.Storage.database.MySQLDatabase;
import no.lambda.client.entur.EnturGraphQLClient;
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

        // Demonstrasjon av opprettelse av en ny rute i databasen.
        // Rute eksempelRute = new Rute(2,1, 60, 120, 40, 80, 3);
        // reiseKlar.createFavoriteRoute(eksempelRute);
    }
}


