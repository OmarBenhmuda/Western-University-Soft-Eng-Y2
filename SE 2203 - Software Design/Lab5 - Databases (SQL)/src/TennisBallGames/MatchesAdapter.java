package TennisBallGames;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Abdelkader
 */
public class MatchesAdapter {

    Connection connection;

    public MatchesAdapter(Connection conn, Boolean reset) throws SQLException {
        connection = conn;
        if (reset) {
            Statement stmt = connection.createStatement();
            try {
                // Remove tables if database tables have been created.
                // This will throw an exception if the tables do not exist
                stmt.execute("DROP TABLE Matches");
                // then do finally
            } catch (SQLException ex) {
                // No need to report an error.
                // The table simply did not exist.
                // do finally to create it
            } finally {
                // Create the table of Matches
                stmt.execute("CREATE TABLE Matches ("
                        + "MatchNumber INT NOT NULL PRIMARY KEY, "
                        + "HomeTeam CHAR(15) NOT NULL REFERENCES Teams (TeamName), "
                        + "VisitorTeam CHAR(15) NOT NULL REFERENCES Teams (TeamName), "
                        + "HomeTeamScore INT, "
                        + "VisitorTeamScore INT "
                        + ")");
                populateSamples();
            }
        }
    }

    private void populateSamples() throws SQLException {
        // Create a listing of the matches to be played
        this.insertMatch(1, "Astros", "Brewers");
        this.insertMatch(2, "Brewers", "Cubs");
        this.insertMatch(3, "Cubs", "Astros");
    }


    public int getMax() throws SQLException {
        int num = 0;

        Statement stmt = connection.createStatement();

        String sqlStatement = "SELECT MAX(MatchNumber) FROM Matches";

        ResultSet rs = stmt.executeQuery(sqlStatement);

        while (rs.next()) {

            num = rs.getInt(1);
        }

        // Add your work code here for Task #3

        return num;
    }

    public void insertMatch(int num, String home, String visitor) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("INSERT INTO Matches (MatchNumber, HomeTeam, VisitorTeam, HomeTeamScore, VisitorTeamScore) "
                + "VALUES (" + num + " , '" + home + "' , '" + visitor + "', 0, 0)");
    }

    // Get all Matches
    public ObservableList<Matches> getMatchesList() throws SQLException {
        ObservableList<Matches> matchesList = FXCollections.observableArrayList();

        Statement stmt = null;

        String sqlStatement = "SELECT * from Matches";


        stmt = connection.createStatement();

        ResultSet rs = stmt.executeQuery(sqlStatement);

        while (rs.next()) {

            matchesList.add(new Matches
                    (
                            rs.getInt("MatchNumber"),
                            rs.getString("HomeTeam"),
                            rs.getString("VisitorTeam")
                    )
            );

        }


        // Add your code here for Task #2

        return matchesList;
    }

    // Get a String list of matches to populate the ComboBox used in Task #4.
    public ObservableList<String> getMatchesNamesList() throws SQLException {
        ObservableList<String> list = FXCollections.observableArrayList();


        Statement stmt = null;

        String sqlStatement = "SELECT * from Matches";


        stmt = connection.createStatement();

        ResultSet rs = stmt.executeQuery(sqlStatement);


        while (rs.next()) {


            int matchNumber = rs.getInt("MatchNumber");
            String homeTeam = rs.getString("HomeTeam");
            String visitorTeam = rs.getString("VisitorTeam");
            String match = matchNumber + "- " + homeTeam + " vs " + visitorTeam;

            list.add(match);

        }


        return list;
    }


    public void setTeamsScore(int matchNumber, int hScore, int vScore) throws SQLException {

        Statement stmt = null;

        String sqlStatement = "SELECT * from Matches";


        stmt = connection.createStatement();

        ResultSet rs = stmt.executeQuery(sqlStatement);


        while (rs.next()) {

            stmt.executeUpdate("INSERT INTO Matches (MatchNumber, HomeTeam, VisitorTeam, HomeTeamScore, VisitorTeamScore) "
                    + "VALUES "
                    + "(" + matchNumber + " , '"
                    + rs.getString("HomeTeam") + "' , '"
                    + rs.getString("AwayTeam") + "', hscore, vscore)");


        }


    }
}
