package MariaTest;

import java.sql.*;


/**
 * Quick test of Maria db connection.
 *
 * Full Credit / Citation:  https://mariadb.com/kb/en/java-connector-using-gradle/
 */
public class App {
    public static void main( String[] args ) throws SQLException {
        //create connection for a server installed in localhost, with a user "root" with no password
        try (Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost/", "root", "mypass")) {
            // create a Statement
            try (Statement stmt = conn.createStatement()) {
                //execute query
                try (ResultSet rs = stmt.executeQuery("SELECT 'Hello World!'")) {
                    //position result to first
                    rs.first();
                    System.out.println(rs.getString(1)); //result is "Hello World!"
                }
            }
        }
    }
}
