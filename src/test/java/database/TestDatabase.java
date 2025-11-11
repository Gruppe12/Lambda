package database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/*
Abstrakt testdatabase for testing av ulike metoder og SQLs-spørringer mot en database.
*/
public abstract class TestDatabase {
    protected Connection connection;

    public abstract Connection startDB() throws Exception;

    public abstract void stopDB() throws Exception;

    //Lager tabeller mot databasen.
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

    //Lager placeholder data som brukes for testing av de ulike metodene.
    public void createDummyData() throws Exception{
        try (Statement statement = connection.createStatement()) {
            lagBruker(1, "Jane", "Doe");
            lagBruker(2, "John", "Doe");
            lagFavorittrute(1, 1, 20.4, 10.5, 49.3, 31.9, 3);
        }
    }

    //Lager en rad i 'Bruker' tabellen med verdier angitt i parameterne.
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

    //Lager en rad i 'Favorittrute' tabllen med verdier angitt i parameterne.
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

    //Teller antall rader i en tabell og gir en tallverdi basert på antall rader. Tabellen som skal telles er angitt i parameteren.
    public int countRowsInTable(String tableName) throws Exception {
        String sql = "SELECT COUNT(*) FROM " + tableName;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        }
    }

    //Returnerer en eller flere spesifikke verdier fra databasen gitt som første parameter basert på gitt favorittruteId gitt i andre parameter.
    public String getValueFromColumnBasedOnFavoriteId(String column, int favorittruteId) throws Exception {
        String sql = "SELECT "+column+" FROM Favorittrute WHERE favorittrute_id="+ favorittruteId;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getString(1);
        }
    }
}

