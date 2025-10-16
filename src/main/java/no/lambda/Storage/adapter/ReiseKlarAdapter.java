package no.lambda.Storage.adapter;
import no.lambda.exception.EnTurException;
import no.lambda.model.Rute;
import no.lambda.port.ReiseKlarPort;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReiseKlarAdapter implements ReiseKlarPort {

    private Connection connection;

    public ReiseKlarAdapter(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void createFavoriteRoute(Rute rute) throws EnTurException {
        String sql = "INSERT INTO Favorittrute(favorittrute_id, bruker_id, from_longitude, from_latitude, to_longitude, to_latitude, to_place_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, rute.getFavorittrute_id());
            preparedStatement.setInt(2, rute.getBruker_id());
            preparedStatement.setDouble(3, rute.getFrom_longitude());
            preparedStatement.setDouble(4, rute.getFrom_latitude());
            preparedStatement.setDouble(5, rute.getTo_longitude());
            preparedStatement.setDouble(6, rute.getTo_latitude());
            preparedStatement.setInt(7, rute.getTo_place_id());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new EnTurException("Could not create favorite route", e);
        }
    }

    @Override
    public Rute getFavoriteRoute(int favorittruteId) throws EnTurException {
        String sql = "SELECT favorittrute_id, bruker_id, from_longitude, from_latitude, to_longitude, to_latitude, to_place_id " +
                     "FROM Favorittrute " +
                     "WHERE favorittrute_id=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, favorittruteId);

            Rute favorittRute = new Rute();

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int brukerId = resultSet.getInt("bruker_id");
                double fromLongitude = resultSet.getInt("from_longitude");
                double fromLatitude = resultSet.getInt("from_latitude");
                double toLongitude = resultSet.getInt("to_longitude");
                double toLatitude = resultSet.getInt("from_latitude");
                int toPlaceId = resultSet.getInt("to_place_id");

                favorittRute.setFavorittrute_id(favorittruteId);
                favorittRute.setBruker_id(brukerId);
                favorittRute.setFrom_longitude(fromLongitude);
                favorittRute.setFrom_latitude(fromLatitude);
                favorittRute.setTo_longitude(toLongitude);
                favorittRute.setTo_latitude(toLatitude);
                favorittRute.setTo_place_id(toPlaceId);
            }

            return favorittRute;

        } catch (SQLException e) {
            throw new EnTurException("Could not retrieve favorite route " + favorittruteId, e);
        }
    }

    @Override
    public void createUserSQLQuery(String fornavn, String etternavn) throws EnTurException {
        //Opprette bruker, "ID er generert automatisk."
        System.out.println("INSERT INTO Bruker(fornavn, etternavn)\n" +
                           "VALUES ("+fornavn+", "+etternavn+")");
    }

    @Override
    public void createFavoriteRouteSQLQuery(int brukerId, double fromLongitude, double fromLatitude, double ToLongitude, double ToLatitude, int ToPlaceId) throws EnTurException {
        //Opprette Favorittrute, "ID er generert automatisk."
        System.out.println("INSERT INTO Favorittrute(bruker_id, from_longitude, from_latitude, to_longitude, to_latitude, to_place_id)\n" +
                           "VALUES ("+brukerId+", "+fromLongitude+", "+fromLatitude+", "+ToLongitude+", "+ToLatitude+", "+ToPlaceId+")");
    }

    @Override
    public void getToAndFromBasedOnFavoriteRouteIDAndUserIDSQLQuery(int favorittruteId, int brukerId) throws EnTurException {
        //Hente from/to longitude/latitude fra en bestemt favorittrute og bruker.
        System.out.println("SELECT from_longitude, from_latitude, to_longitude, to_latitude\n" +
                           "FROM Favorittrute\n" +
                           "WHERE favorittrute_id = "+favorittruteId+" AND bruker_id = "+brukerId);
    }

    @Override
    public void getFavoriteRoutesFromUserBasedOnIdSQLQuery(int brukerId) throws EnTurException {
        //Hente favorittruter fra en bestemt bruker
        System.out.println("SELECT *\n" +
                           "FROM Favorittrute\n" +
                           "WHERE bruker_id = "+brukerId);
    }
}




