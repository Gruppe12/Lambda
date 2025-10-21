package no.lambda.test.unittest;

import no.lambda.Storage.adapter.ReiseKlarAdapter;
import no.lambda.Storage.adapter.SQLStringAdapter;
import no.lambda.Storage.database.MySQLDatabase;
import no.lambda.port.SQLStringPort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import no.lambda.port.ReiseKlarPort;
import java.sql.Connection;
import java.util.ArrayList;


//Tester om de ulike SQL String metodene returnerer den String teksten som forventet.
public class ReiseKlarUnitTests {

    @Test
    public void SQLStringAdapter_StringsCreatedSuccessfully() throws Exception {
        //Arrange
        SQLStringAdapter sqlStringAdapter = new SQLStringAdapter();

        //Act
        String stringText1 = sqlStringAdapter.createUserSQLQuery("Jane", "Doe");
        String stringText2 = sqlStringAdapter.createFavoriteRouteSQLQuery(1, 10, 20, 30, 40, 1);
        String stringText3 = sqlStringAdapter.getToAndFromBasedOnFavoriteRouteIDAndUserIDSQLQuery(1, 1);
        String stringText4 = sqlStringAdapter.getFavoriteRoutesFromUserBasedOnIdSQLQuery(1);

        //Assert
        Assertions.assertEquals(("INSERT INTO Bruker(fornavn, etternavn)\n" + "VALUES (Jane, Doe)"), stringText1);
        Assertions.assertEquals(("INSERT INTO Favorittrute(bruker_id, from_longitude, from_latitude, to_longitude, to_latitude, to_place_id)\n" +
                "VALUES ("+1+", "+10.0+", "+20.0+", "+30.0+", "+40.0+", "+1+")"), stringText2);
        Assertions.assertEquals(("SELECT from_longitude, from_latitude, to_longitude, to_latitude\n" +
                "FROM Favorittrute\n" +
                "WHERE favorittrute_id = "+1+" AND bruker_id = "+1+")"), stringText3);
        Assertions.assertEquals(("SELECT *\n" +
                "FROM Favorittrute\n" +
                "WHERE bruker_id = "+1+")"), stringText4);
    }
}



