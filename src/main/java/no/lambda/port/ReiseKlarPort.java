package no.lambda.port;
import no.lambda.Storage.exception.MySQLDatabaseException;
import no.lambda.exception.EnTurException;
import no.lambda.model.Bruker;
import no.lambda.model.Rute;

import java.util.ArrayList;

public interface ReiseKlarPort {

    void createFavoriteRoute(Rute rute) throws MySQLDatabaseException;
    void createFavoriteRouteWithoutFavoriteId(Rute rute) throws MySQLDatabaseException;
    void createUser(String fornavn, String etternavn) throws MySQLDatabaseException;
    Rute getFavoriteRoute(int favorittruteId) throws MySQLDatabaseException;
    ArrayList<Double> getToAndFromBasedOnFavoriteRouteIDAndUserID(int favorittruteId, int brukerId) throws MySQLDatabaseException;
    ArrayList<ArrayList<Double>> getFavoriteRoutesFromUserBasedOnId(int brukerId) throws MySQLDatabaseException;
    void deleteUserBasedOnFavoriteRouteId(int favorittruteId) throws MySQLDatabaseException;
}






