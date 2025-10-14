package no.lambda.test.database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public abstract class TestDatabase {
    protected Connection connection;

    public abstract Connection startDB() throws Exception;

    public abstract void stopDB() throws Exception;

    public void createTables() throws Exception{
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE Bruker(\n" +
                    "\tbruker_id INT AUTO_INCREMENT,\n" +
                    "    fornavn VARCHAR(32),\n" +
                    "    etternavn VARCHAR(32),\n" +
                    "    CONSTRAINT BrukerPN\n" +
                    "      PRIMARY KEY (bruker_id)\n" +
                    ")");

            statement.execute("CREATE TABLE Favorittrute(\n" +
                    "    favorittrute_id INT AUTO_INCREMENT,\n" +
                    "    bruker_id INT,\n" +
                    "    from_longitude DOUBLE,\n" +
                    "    from_latitude DOUBLE,\n" +
                    "    to_longitude DOUBLE,\n" +
                    "    to_latitude DOUBLE,\n" +
                    "    to_place_id VARCHAR(32),\n" +
                    "    CONSTRAINT FavorittrutePN\n" +
                    "      PRIMARY KEY (favorittrute_id),\n" +
                    "\tCONSTRAINT FavorittruteBrukerFN\n" +
                    "      FOREIGN KEY (bruker_id)\n" +
                    "      REFERENCES Bruker (bruker_id)\n" +
                    ")");
        }
    }

    public void createDummyData() throws Exception{
        try (Statement statement = connection.createStatement()) {
            lagBruker(1, "Jane", "Doe");
            lagBruker(2, "John", "Doe");
            lagFavorittrute(1, 1, 20.4, 10.5, 49.3, 31.9, 3);
        }
    }

    public void lagBruker(int brukerId, String fornavn, String etternavn)
            throws Exception{
        String sql = "INSERT INTO Bruker (bruker_id, fornavn, etternavn) " +
                "VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, brukerId);
            preparedStatement.setString(2, fornavn);
            preparedStatement.setString(3, etternavn);
            preparedStatement.executeUpdate();
        }
    }

    public void lagFavorittrute(int favorittruteId, int brukerId, double fromLongitude, double fromLatitude, double toLongitude, double toLatitude, int toPlaceId) throws Exception{
        String sql = "INSERT INTO Favorittrute(favorittrute_id, bruker_id, from_longitude, from_latitude, to_longitude, to_latitude, to_place_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, favorittruteId);
            preparedStatement.setInt(2, brukerId);
            preparedStatement.setDouble(3, fromLongitude);
            preparedStatement.setDouble(4, fromLatitude);
            preparedStatement.setDouble(5, toLongitude);
            preparedStatement.setDouble(6, toLatitude);
            preparedStatement.setInt(7, toPlaceId);
            preparedStatement.executeUpdate();
        }
    }

    public int countRowsInTable(String tableName) throws Exception {
        String sql = "SELECT COUNT(*) FROM " + tableName;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        }
    }

    public String getSpecificFromLongitudeValue(int favorittruteId) throws Exception{
        String sql = "SELECT from_longitude FROM Favorittrute WHERE favorittrute_id=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, favorittruteId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getString("from_longitude");
        }
    }
}

