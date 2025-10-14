package no.lambda.test.integrationtest;
import no.lambda.Storage.adapter.ReiseKlarAdapter;
import no.lambda.model.Rute;
import no.lambda.test.database.H2TestDatabase;
import no.lambda.test.database.TestDatabase;
import org.junit.jupiter.api.*;
import java.sql.Connection;

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

    @Test
    public void lagFavorittrute_favorittruteErLagetRiktig() throws Exception {
        //Arrange
        Rute rute = new Rute(2, 2, 20.1, 14.7, 46.5, 70.0, 4);

        //Act
        reiseKlar.createFavoriteRoute(rute);

        //Assert
        Assertions.assertEquals(2, testDB.countRowsInTable(("Favorittrute")));
        Assertions.assertEquals("20.1", testDB.getSpecificFromLongitudeValue(2));
    }
}
