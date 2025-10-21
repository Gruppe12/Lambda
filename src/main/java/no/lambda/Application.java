package no.lambda;
import no.lambda.Services.EnturService;
import no.lambda.Storage.adapter.ReiseKlarAdapter;
import no.lambda.Storage.database.MySQLDatabase;
import no.lambda.client.entur.EnturGraphQLClient;
import no.lambda.port.ReiseKlarPort;

import java.sql.Connection;

public class Application {
    private final static String URL = "jdbc:mysql://localhost:3306/entur";
    private final static String USERNAME = "username";
    private final static String PASSWORD = "password";

    public static void main(String[] args) throws Exception {

        // Konfigurerer database
        MySQLDatabase database = new MySQLDatabase(URL, USERNAME, PASSWORD);
        Connection dbConnection = database.startDB();

        // Konfigurerer klasse (for database-spørringer)
        ReiseKlarPort enTur = new ReiseKlarAdapter(dbConnection);

        EnturGraphQLClient client = new EnturGraphQLClient();
        // Oppretter EnturService
        EnturService enturService = new EnturService(client, enTur);
    }
}


