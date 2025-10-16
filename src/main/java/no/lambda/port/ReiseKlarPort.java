package no.lambda.port;
import no.lambda.exception.EnTurException;
import no.lambda.model.Rute;

public interface ReiseKlarPort {

    void createFavoriteRoute(Rute rute) throws EnTurException;
    Rute getFavoriteRoute(int favorittruteId) throws EnTurException;
    void createUserSQLQuery(String fornavn, String etternavn) throws EnTurException;
    void createFavoriteRouteSQLQuery(int brukerId, double fromLongitude, double fromLatitude, double ToLongitude, double ToLatitude, int ToPlaceId) throws EnTurException;
    void getToAndFromBasedOnFavoriteRouteIDAndUserIDSQLQuery(int favorittruteId, int brukerId) throws EnTurException;
    void getFavoriteRoutesFromUserBasedOnIdSQLQuery(int brukerId) throws EnTurException;
}


