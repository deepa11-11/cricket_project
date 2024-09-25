import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class CricketTournamentScheduler {

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/cricket_tournament";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";

    public static void main(String[] args) {
        // Get the list of teams from the database
        ArrayList<String> teams = getTeamsFromDatabase();

        if (teams.size() < 2) {
            System.out.println("Not enough teams to schedule matches.");
            return;
        }

        // Schedule the matches
        ArrayList<Match> matches = scheduleMatches(teams);

        // Display the schedule
        System.out.println("Cricket Tournament Schedule:");
        for (int i = 0; i < matches.size(); i++) {
            Match match = matches.get(i);
            System.out.println("Match " + (i + 1) + ": " + match.getTeam1() + " vs " + match.getTeam2() + 
                               " | Date: " + match.getDate() + " | Time: " + match.getTime() + 
                               " | Format: " + match.getFormat());
        }
    }

    // Function to get teams from the database
    public static ArrayList<String> getTeamsFromDatabase() {
        ArrayList<String> teams = new ArrayList<>();

        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Create a connection to the database
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // SQL query to retrieve team names from the 'teams' table
            String query = "SELECT team_name FROM teams";
            PreparedStatement stmt = conn.prepareStatement(query);

            // Execute the query and get the result set
            ResultSet rs = stmt.executeQuery();

            // Iterate over the result set to get team names
            while (rs.next()) {
                teams.add(rs.getString("team_name"));
            }

            // Close the result set, statement, and connection
            rs.close();
            stmt.close();
            conn.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return teams;
    }

    // Function to schedule matches in a round-robin format with date, time, and format
    public static ArrayList<Match> scheduleMatches(ArrayList<String> teams) {
        ArrayList<Match> matchList = new ArrayList<>();

        int numberOfTeams = teams.size();
        String[] formats = {"T20", "ODI", "Test"}; // Different match formats
        Random rand = new Random();

        // Get the current date
        Calendar calendar = Calendar.getInstance();

        // Round-robin scheduling: Each team plays against every other team
        for (int i = 0; i < numberOfTeams - 1; i++) {
            for (int j = i + 1; j < numberOfTeams; j++) {
                // Increment date for each match
                calendar.add(Calendar.DAY_OF_MONTH, 2); // Add 2 days between each match
                
                // Format date and time
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

                // Generate match details
                String matchDate = dateFormat.format(calendar.getTime());
                String matchTime = timeFormat.format(rand.nextInt(12) + 10) + ":00"; // Random time between 10 AM - 10 PM
                String matchFormat = formats[rand.nextInt(formats.length)]; // Random format

                // Create a new match
                Match match = new Match(teams.get(i), teams.get(j), matchDate, matchTime, matchFormat);
                matchList.add(match);
            }
        }
        return matchList;
    }
}

// Match class to represent a match between two teams with date, time, and format
class Match {
    private String team1;
    private String team2;
    private String date;
    private String time;
    private String format;

    public Match(String team1, String team2, String date, String time, String format) {
        this.team1 = team1;
        this.team2 = team2;
        this.date = date;
        this.time = time;
        this.format = format;
    }

    public String getTeam1() {
        return team1;
    }

    public String getTeam2() {
        return team2;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getFormat() {
        return format;
    }
}