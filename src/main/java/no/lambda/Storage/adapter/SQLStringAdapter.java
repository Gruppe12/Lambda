package no.lambda.Storage.adapter;

import no.lambda.Storage.exception.MySQLDatabaseException;
import no.lambda.exception.EnTurException;
import no.lambda.port.SQLStringPort;

/*
Inneholder flere metoder som produserer dynamiske SQL queries basert på gitte parametere.
 */
public class SQLStringAdapter implements SQLStringPort {

    @Override
    public String createFavoriteRouteSQLQuery(int brukerId, double fromLongitude, double fromLatitude, double ToLongitude, double ToLatitude, int ToPlaceId) throws MySQLDatabaseException {
        //Opprette Favorittrute, "ID er generert automatisk."
        return "INSERT INTO Favorittrute(bruker_id, from_longitude, from_latitude, to_longitude, to_latitude, to_place_id)\n" +
                "VALUES ('"+brukerId+"', '"+fromLongitude+"', '"+fromLatitude+"', '"+ToLongitude+"', '"+ToLatitude+"', '"+ToPlaceId+"')";
    }

    @Override
    public String createUserSQLQuery(String fornavn, String etternavn) throws MySQLDatabaseException {
        //Opprette bruker, "ID er generert automatisk."
        return "INSERT INTO Bruker(fornavn, etternavn)\n" +
                "VALUES ('"+fornavn+"', '"+etternavn+"')";
    }

    @Override
    public String getToAndFromBasedOnFavoriteRouteIDAndUserIDSQLQuery(int favorittruteId, int brukerId) throws MySQLDatabaseException {
        //Hente from/to longitude/latitude fra en bestemt favorittrute og bruker.
        return "SELECT from_longitude, from_latitude, to_longitude, to_latitude\n" +
                "FROM Favorittrute\n" +
                "WHERE favorittrute_id = "+favorittruteId+" AND bruker_id = "+brukerId;
    }

    @Override
    public String getFavoriteRoutesFromUserBasedOnIdSQLQuery(int brukerId) throws MySQLDatabaseException {
        //Hente favorittruter fra en bestemt bruker
        return "SELECT *\n" +
                "FROM Favorittrute\n" +
                "WHERE bruker_id = "+brukerId;
    }

    @Override
    public String deleteUserBasedOnFavoriteRouteIdSQLQuery(int favorittruteId) throws MySQLDatabaseException {
        return "DELETE FROM Favorittrute WHERE favorittrute_id = "+favorittruteId;
    }

    @Override
    public String checkIfFavoriteRouteAlreadyExistsSQLQuery(int brukerId, double fromLongitude, double fromLatitude, double ToLongitude, double ToLatitude) {
        return "SELECT * FROM Favorittrute WHERE bruker_id = "+brukerId+" AND from_longitude = "+fromLongitude+" AND from_latitude = "+fromLatitude+" AND to_longitude = "+ToLongitude+" AND to_latitude = "+ToLatitude;
    }
}


