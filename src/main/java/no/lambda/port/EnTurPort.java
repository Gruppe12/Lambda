package no.lambda.port;
import no.lambda.exception.EnTurException;
import no.lambda.model.Rute;
import java.util.ArrayList;

public interface EnTurPort {

    void createRoute(Rute rute) throws EnTurException;
    void createFavoriteRoute(Rute rute) throws EnTurException;
}
