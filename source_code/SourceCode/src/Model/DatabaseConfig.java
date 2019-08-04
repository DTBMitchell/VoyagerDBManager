package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {

    private static final String USERNAME = "kevin";
    private static final String PASSWORD = "Qg3gdDXBBvWEkY18";
    private static final String CONN_STRING = "jdbc:mysql://localhost/voyager";

    // connection  method that connects us to the MySQL database
    public static Connection getConnection() throws SQLException {
        //System.out.println("Connected to student database successfully!");
        return DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
    }

    // method that displays our errors in more detail if connection fails
    public static void displayException(SQLException ex){

        System.err.println("Error Message: " + ex.getMessage());
        System.err.println("Error Code: " + ex.getErrorCode());
        System.err.println("SQL Status: " + ex.getSQLState());

    }

    /*
     public int saveUserIntoDatabase(String username, String email, String password, String salt) {
        int affectedRow=0;
        String query = "insert into users" + "(username, email, password, salt)"
                + "values(?,?,?,?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement sqlStatement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);) {
            sqlStatement.setString(1, username);
            sqlStatement.setString(2, email);
            sqlStatement.setString(3, password);
            sqlStatement.setString(4, salt);

            // get the number of return rows
            affectedRow = sqlStatement.executeUpdate();

        } catch (Exception e) {
            System.out.println("Status: operation failed due to " + e);

        }
        return affectedRow;

    }
     */
}