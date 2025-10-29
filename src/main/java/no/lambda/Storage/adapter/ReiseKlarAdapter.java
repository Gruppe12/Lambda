package no.lambda.Storage.adapter;
import no.lambda.Storage.exception.MySQLDatabaseException;
import no.lambda.exception.EnTurException;
import no.lambda.model.Bruker;
import no.lambda.model.Rute;
import no.lambda.port.ReiseKlarPort;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
Adapter for ReiseKlar løsningen som inneholder logikk for kommunikasjon med databasen.
*/
public class ReiseKlarAdapter implements ReiseKlarPort {

    private Connection connection;
    SQLStringAdapter sqlStringAdapter = new SQLStringAdapter();

    public ReiseKlarAdapter(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void createFavoriteRoute(Rute rute) throws MySQLDatabaseException {
        //Lager en ny rad i 'Favorittrute' tabellen basert på verdier inneholdt i et ruteobjekt.
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
    public void createUser(String fornavn, String etternavn) throws MySQLDatabaseException {
        //Lager en ny rad i 'Bruker' tabellen basert på verdier inneholdt i et ruteobjekt.
        String sql = sqlStringAdapter.createUserSQLQuery(fornavn, etternavn);

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new EnTurException("Could not create user", e);
        }
    }

    @Override
    public Rute getFavoriteRoute(int favorittruteId) throws MySQLDatabaseException {
        //Henter en favorittrute fra databasen basert på gitt id i parameter og lager et nytt ruteobjekt basert på verdiene i den raden.
        String sql = "SELECT favorittrute_id, bruker_id, from_longitude, from_latitude, to_longitude, to_latitude, to_place_id " +
                     "FROM Favorittrute " +
                     "WHERE favorittrute_id=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, favorittruteId);

            Rute favorittRute = new Rute();

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int brukerId = resultSet.getInt("bruker_id");
                double fromLongitude = resultSet.getDouble("from_longitude");
                double fromLatitude = resultSet.getDouble("from_latitude");
                double toLongitude = resultSet.getDouble("to_longitude");
                double toLatitude = resultSet.getDouble("to_latitude");
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
            throw new EnTurException("Could not retrieve favorite route" + favorittruteId, e);
        }
    }

    @Override
    public String getToAndFromBasedOnFavoriteRouteIDAndUserID(int favorittruteId, int brukerId) throws MySQLDatabaseException {
        //Hente from/to longitude/latitude fra en bestemt favorittrute og bruker, og skrive ut informasjonen som blir hentet.
        String sql = sqlStringAdapter.getToAndFromBasedOnFavoriteRouteIDAndUserIDSQLQuery(favorittruteId, brukerId);

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getString(1) + " " +
                    resultSet.getString(2) + " " +
                    resultSet.getString(3) + " " +
                    resultSet.getString(4);
        } catch (SQLException e) {
            throw new EnTurException("Could not get to and from based on Ids", e);
        }
    }

    @Override
    public String getFavoriteRoutesFromUserBasedOnId(int brukerId) throws MySQLDatabaseException {
        //Hente favorittruter fra en bestemt bruker, og skrive ut informasjonen som blir hentet.
        String sql = sqlStringAdapter.getFavoriteRoutesFromUserBasedOnIdSQLQuery(brukerId);

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getString(1) + " " +
                    resultSet.getString(2) + " " +
                    resultSet.getString(3) + " " +
                    resultSet.getString(4) + " " +
                    resultSet.getString(5) + " " +
                    resultSet.getString(6) + " " +
                    resultSet.getString(7);
        } catch (SQLException e) {
            throw new EnTurException("Could not get to and from based on Ids", e);
        }
    }
}




