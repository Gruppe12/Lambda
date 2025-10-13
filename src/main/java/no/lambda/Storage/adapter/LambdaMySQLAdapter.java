package no.lambda.Storage.adapter;
import no.lambda.exception.EnTurException;
import no.lambda.model.Rute;
import no.lambda.port.EnTurPort;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LambdaMySQLAdapter implements EnTurPort {

    private Connection connection;

    public LambdaMySQLAdapter(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void createRoute(Rute rute) throws EnTurException {
        String sql = "INSERT INTO rute (rute_navn) VALUES (?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, rute.getNavn());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new EnTurException("Could not create route", e);
        }
    }

    @Override
    public void createFavoriteRoute(Rute rute) throws EnTurException{
        String sql = "SELECT favorittrute_id, bruker_id, from_longitude, from_latitude, to_longitude, to_latitude, to_place_id " +
                "FROM Favorittrute ";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                continue;
            }

        } catch (SQLException e) {
            throw new EnTurException("Could not create favorite route " + rute.getNavn(), e);
        }
    }
}
