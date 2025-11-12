package no.lambda.port;

import no.lambda.Storage.exception.MySQLDatabaseException;
import no.lambda.exception.EnTurException;

public interface SQLStringPort {
    String createFavoriteRouteSQLQuery(int brukerId, double fromLongitude, double fromLatitude, double ToLongitude, double ToLatitude, int ToPlaceId) throws MySQLDatabaseException;
    String createUserSQLQuery(String fornavn, String etternavn) throws MySQLDatabaseException;
    String getToAndFromBasedOnFavoriteRouteIDAndUserIDSQLQuery(int favorittruteId, int brukerId) throws MySQLDatabaseException;
    String getFavoriteRoutesFromUserBasedOnIdSQLQuery(int brukerId) throws MySQLDatabaseException;
    String deleteUserBasedOnFavoriteRouteIdSQLQuery(int favorittruteId) throws MySQLDatabaseException;
    String checkIfFavoriteRouteAlreadyExistsSQLQuery(int brukerId, double fromLongitude, double fromLatitude, double ToLongitude, double ToLatitude);
}
