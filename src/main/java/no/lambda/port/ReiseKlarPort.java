package no.lambda.port;
import no.lambda.exception.EnTurException;
import no.lambda.model.Rute;

public interface ReiseKlarPort {

    void createFavoriteRoute(Rute rute) throws EnTurException;
    Rute getFavoriteRoute(int favorittruteId) throws EnTurException;
}
