package no.lambda.port;

import no.lambda.exception.EnTurException;

public interface SQLStringPort {
    String createUserSQLQuery(String fornavn, String etternavn) throws EnTurException;
    String createFavoriteRouteSQLQuery(int brukerId, double fromLongitude, double fromLatitude, double ToLongitude, double ToLatitude, int ToPlaceId) throws EnTurException;
    String getToAndFromBasedOnFavoriteRouteIDAndUserIDSQLQuery(int favorittruteId, int brukerId) throws EnTurException;
    String getFavoriteRoutesFromUserBasedOnIdSQLQuery(int brukerId) throws EnTurException;
}
