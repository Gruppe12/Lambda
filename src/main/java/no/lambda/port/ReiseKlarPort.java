package no.lambda.port;
import no.lambda.Storage.exception.MySQLDatabaseException;
import no.lambda.exception.EnTurException;
import no.lambda.model.Bruker;
import no.lambda.model.Rute;

public interface ReiseKlarPort {

    void createFavoriteRoute(Rute rute) throws MySQLDatabaseException;
    void createUser(String fornavn, String etternavn) throws MySQLDatabaseException;
    Rute getFavoriteRoute(int favorittruteId) throws MySQLDatabaseException;
    String getToAndFromBasedOnFavoriteRouteIDAndUserID(int favorittruteId, int brukerId) throws MySQLDatabaseException;
    String getFavoriteRoutesFromUserBasedOnId(int brukerId) throws MySQLDatabaseException;
}


