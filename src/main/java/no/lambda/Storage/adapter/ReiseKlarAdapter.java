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
import java.util.ArrayList;

/*
Adapter for ReiseKlar løsningen som inneholder logikk for kommunikasjon med databasen.
*/
public class ReiseKlarAdapter implements ReiseKlarPort {

    private Connection connection;
    SQLStringAdapter sqlStringAdapter = new SQLStringAdapter();

    public ReiseKlarAdapter(Connection connection) {
        this.connection = connection;
    }

   /* @Override
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
    } */

    @Override
    public int createFavoriteRouteWithoutFavoriteId(Rute rute) throws MySQLDatabaseException {
        //Lager en ny rad i 'Favorittrute' tabellen basert på verdier inneholdt i et ruteobjekt uten å må legge til favorittrute_id, og returnerer den favorittrute_id hvis den fins.
        String sql = "INSERT INTO Favorittrute(bruker_id, from_longitude, from_latitude, to_longitude, to_latitude, to_place_id) VALUES (?, ?, ?, ?, ?, ?)";
        String sql2 = sqlStringAdapter.getFavoriteRouteIdSQLQuery(rute.getBruker_id(), rute.getFrom_longitude(), rute.getFrom_latitude(), rute.getTo_longitude(), rute.getTo_latitude());

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, rute.getBruker_id());
            preparedStatement.setDouble(2, rute.getFrom_longitude());
            preparedStatement.setDouble(3, rute.getFrom_latitude());
            preparedStatement.setDouble(4, rute.getTo_longitude());
            preparedStatement.setDouble(5, rute.getTo_latitude());
            preparedStatement.setInt(6, rute.getTo_place_id());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new EnTurException("Could not create favorite route without f_id", e);
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql2)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            throw new EnTurException("Could not get f_id", e);
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

                //favorittRute.setFavorittrute_id(favorittruteId);
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
    public ArrayList<Double> getToAndFromBasedOnFavoriteRouteIDAndUserID(int favorittruteId, int brukerId) throws MySQLDatabaseException {
        //Hente liste av from/to longitude/latitude verdier fra en bestemt favorittrute og bruker, og skrive ut informasjonen som blir hentet.
        ArrayList<Double> coordinates = new ArrayList<>();
        String sql = sqlStringAdapter.getToAndFromBasedOnFavoriteRouteIDAndUserIDSQLQuery(favorittruteId, brukerId);

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            coordinates.add(resultSet.getDouble(1));
            coordinates.add(resultSet.getDouble(2));
            coordinates.add(resultSet.getDouble(3));
            coordinates.add(resultSet.getDouble(4));
            return coordinates;
        } catch (SQLException e) {
            throw new EnTurException("Could not get to and from based on Ids", e);
        }
    }

    @Override
    public ArrayList<ArrayList<Double>> getFavoriteRoutesFromUserBasedOnId(int brukerId) throws MySQLDatabaseException {
        //Hente favorittrute koordinater fra en bestemt bruker, og skrive ut informasjonen som blir hentet.
        ArrayList<ArrayList<Double>> amountOfFavorites = new ArrayList<>();
        String sql = sqlStringAdapter.getFavoriteRoutesFromUserBasedOnIdSQLQuery(brukerId);

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ArrayList<Double> coordinates = new ArrayList<>();
                //fromLongitude
                coordinates.add(resultSet.getDouble(3));
                //fromLatitude
                coordinates.add(resultSet.getDouble(4));
                //toLongitude
                coordinates.add(resultSet.getDouble(5));
                //toLatitude
                coordinates.add(resultSet.getDouble(6));
                //bruker_id
                coordinates.add(resultSet.getDouble(1));
                amountOfFavorites.add(coordinates);
            }
            return amountOfFavorites;
        } catch (SQLException e) {
            throw new EnTurException("Could not get favorite routes based on Id", e);
        }
    }

    @Override
    public void deleteUserBasedOnFavoriteRouteId(int favorittruteId) throws MySQLDatabaseException {
        //Sletter en rad fra Favorittrute-tabellen basert på favorittruteId
        String sql = sqlStringAdapter.deleteUserBasedOnFavoriteRouteIdSQLQuery(favorittruteId);

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new EnTurException("Could not delete favorite route", e);
        }
    }

    @Override
    public int checkIfFavoriteRouteAlreadyExists(int brukerId, double fromLongitude, double fromLatitude, double ToLongitude, double ToLatitude) {
        //Sjekker om en favorittrute eksisterer for en bruker.
        String sql = sqlStringAdapter.checkIfFavoriteRouteAlreadyExistsSQLQuery(brukerId, fromLongitude, fromLatitude, ToLongitude, ToLatitude);

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next() == true) {
                return resultSet.getInt(1);
            } else {
                return 0;
            }
        } catch (SQLException e) {
            throw new EnTurException("Could not get favorite routes based on Id", e);
        }
    }
}



