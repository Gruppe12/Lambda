package integrationtest;
import database.TestDatabase;
import no.lambda.Storage.adapter.ReiseKlarAdapter;
import no.lambda.model.Rute;
import database.H2TestDatabase;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;

//Ulike tester for metoder man kan bruke mot testdatabasen.
public class ReiseKlarMySQLAdapterIntegrationTest {
    private static TestDatabase testDB = new H2TestDatabase();
    private static Connection connection;

    private static ReiseKlarAdapter reiseKlar;

    @BeforeAll
    public static void setUpTestDB() throws Exception {
        connection = testDB.startDB();
        testDB.createTables();
        connection.setAutoCommit(false);
        reiseKlar = new ReiseKlarAdapter(connection);
    }

    @BeforeEach
    public void prepareTests() throws Exception{
        testDB.createDummyData();
    }

    @AfterEach
    public void cleanUpTest() throws Exception {
        connection.rollback();
    }

    @AfterAll
    public static void tearDownTestDB() throws Exception {
        testDB.stopDB();
    }

    //Tester om en favorittrute er laget riktig og samsvarer med det som blir lagret i testdatabasen.
    @Test
    public void lagFavorittrute_favorittruteErLagetRiktig() throws Exception {
        //Arrange
        Rute rute = new Rute(2, 2, 20.1, 14.7, 46.5, 70.0, 4);
        Rute rute2 = new Rute(3, 2, 20.2, 14.8, 46.6, 70.1, 5);

        //Act
        reiseKlar.createFavoriteRoute(rute);
        reiseKlar.createFavoriteRouteWithoutFavoriteId(rute2);

        //Assert
        //Tester om antall rader i databasen er det som forventet.
        Assertions.assertEquals(3, testDB.countRowsInTable(("Favorittrute")));
        //Tester om raden i databasen inneholder de forventede verdiene fra rute basert på gitt favorittruteId.
        Assertions.assertEquals("2", testDB.getValueFromColumnBasedOnFavoriteId("bruker_id", 2));
        Assertions.assertEquals("20.1", testDB.getValueFromColumnBasedOnFavoriteId("from_longitude", 2));
        Assertions.assertEquals("14.7", testDB.getValueFromColumnBasedOnFavoriteId("from_latitude", 2));
        Assertions.assertEquals("46.5", testDB.getValueFromColumnBasedOnFavoriteId("to_longitude", 2));
        Assertions.assertEquals("70.0", testDB.getValueFromColumnBasedOnFavoriteId("to_latitude", 2));
        Assertions.assertEquals("4", testDB.getValueFromColumnBasedOnFavoriteId("to_place_id", 2));
        //Tester om raden i databasen inneholder de forventede verdiene fra rute2 basert på gitt favorittruteId.
        Assertions.assertEquals("2", testDB.getValueFromColumnBasedOnFavoriteId("bruker_id", 3));
        Assertions.assertEquals("20.2", testDB.getValueFromColumnBasedOnFavoriteId("from_longitude", 3));
        Assertions.assertEquals("14.8", testDB.getValueFromColumnBasedOnFavoriteId("from_latitude", 3));
        Assertions.assertEquals("46.6", testDB.getValueFromColumnBasedOnFavoriteId("to_longitude", 3));
        Assertions.assertEquals("70.1", testDB.getValueFromColumnBasedOnFavoriteId("to_latitude", 3));
        Assertions.assertEquals("5", testDB.getValueFromColumnBasedOnFavoriteId("to_place_id", 3));
    }

    //Tester om en favorittrute er hentet fra databasen og riktig verdier er returnert.
    @Test
    public void hentFavorittrute_favorittruteErHentetRiktig() throws Exception {
        //Arrange
        int favorittRuteId = 1;
        ArrayList<ArrayList<Double>> favoritesList = new ArrayList<>();
        ArrayList<Double> favoriteItem = new ArrayList<>();
        favoriteItem.add(20.4);
        favoriteItem.add(10.5);
        favoriteItem.add(49.3);
        favoriteItem.add(31.9);
        favoriteItem.add(1.0);
        favoritesList.add(favoriteItem);

        //Act
        Rute hentetFavorittrute = reiseKlar.getFavoriteRoute(favorittRuteId);

        //Assert
        //Tester om verdiene fra ruten laget av 'getFavoriteRoute' metoden er riktige.
        Assertions.assertEquals(1, hentetFavorittrute.getFavorittrute_id());
        Assertions.assertEquals(1, hentetFavorittrute.getBruker_id());
        Assertions.assertEquals(20.4, hentetFavorittrute.getFrom_longitude());
        Assertions.assertEquals(10.5, hentetFavorittrute.getFrom_latitude());
        Assertions.assertEquals(49.3, hentetFavorittrute.getTo_longitude());
        Assertions.assertEquals(31.9, hentetFavorittrute.getTo_latitude());
        Assertions.assertEquals(3, hentetFavorittrute.getTo_place_id());
        //
        Assertions.assertEquals(favoritesList, reiseKlar.getFavoriteRoutesFromUserBasedOnId(1));
    }

    //Tester om en favorittrute blir slettet på korrekt vis.
    @Test
    public void slettFavorittrute_favorittruteErSlettetRiktig() throws Exception {
        //Arrange
        Rute rute1 = new Rute(3, 1, 10, 15, 4, 20, 1);
        Rute rute2 = new Rute(4, 2, 12, 13, 8, 19, 1);
        Rute rute3 = new Rute(5, 2, 16, 11, 5, 18, 1);

        //Act
        reiseKlar.createFavoriteRoute(rute1);
        reiseKlar.createFavoriteRoute(rute2);
        reiseKlar.createFavoriteRoute(rute3);
        reiseKlar.deleteUserBasedOnFavoriteRouteId(4);

        //Assert
        //Tester om antall rader i databasen er det som forventet etter at en rad er slettet fra den.
        Assertions.assertEquals(3, testDB.countRowsInTable(("Favorittrute")));
    }
}


