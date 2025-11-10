package no.lambda.Services;
import no.lambda.Storage.adapter.ReiseKlarAdapter;
import no.lambda.Storage.adapter.SQLStringAdapter;
import no.lambda.Storage.database.MySQLDatabase;
import no.lambda.model.Rute;

import java.sql.Connection;


public class DbService {
    private final static String URL = "jdbc:mysql://itstud.hiof.no:3306/se25_G12";
    private final static String USERNAME = "gruppe12";
    private final static String PASSWORD = "Summer31";

    public static void main(String[] args) throws Exception {
        //Starter databasen.
        connectToDatabase();
    }

    public static void connectToDatabase() throws Exception{
        // Konfigurerer database
        MySQLDatabase database = new MySQLDatabase(URL, USERNAME, PASSWORD);
        Connection dbConnection = database.startDB();

        // Konfigurerer klasser (for database-spørringer)
        var sqlStringAdapter = new SQLStringAdapter();
        var reiseKlarAdapter = new ReiseKlarAdapter(dbConnection);

        //Demonstrasjon av opprettelser av nye rader i databasen.
        //Rute eksempelRute1 = new Rute(1, 60.0, 120.0, 40.0, 80.0, 3);
        //Rute eksempelRute2 = new Rute(2, 35.0, 41.0, 57.0, 55.0, 5);
        //reiseKlarAdapter.createFavoriteRouteWithoutFavoriteId(eksempelRute1);
        //reiseKlarAdapter.createFavoriteRouteWithoutFavoriteId(eksempelRute2);
        //reiseKlarAdapter.createUser("John", "Doe");

        //Demonstrasjon av henting av data fra rader i databasen.
        //System.out.println("Henter favorittrute:\n" + reiseKlarAdapter.getFavoriteRoute(1) + "\n");
        //System.out.println("Henter en liste av fra og til verdier basert på favorittrute_id og bruker_id:\n" + reiseKlarAdapter.getToAndFromBasedOnFavoriteRouteIDAndUserID(1, 1) + "\n");
        //1. fromLongitude 2. fromLatitude 3. toLongitude 4. toLatitude
        //System.out.println("Henter en liste av favorittrutekoordinater basert på bruker_id:\n" + reiseKlarAdapter.getFavoriteRoutesFromUserBasedOnId(1) + "\n");
    }
}


