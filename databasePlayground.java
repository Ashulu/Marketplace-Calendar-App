import java.sql.*;

/**
 * Project Name
 * <p>
 * Project Description
 *
 * @author Gunyoung Park
 */

public class databasePlayground {

    static String URL = "jdbc:sqlite:marketplacedb.db";

    public static void main(String[] args) {

        // Open a connection
        try(Connection conn = DriverManager.getConnection(URL);
            Statement stmt = conn.createStatement();
        ) {
            String sql = "SELECT * FROM accounts";
            ResultSet result = stmt.executeQuery(sql);
            while (result.next()) {
                String email = result.getString("email");
                String password = result.getString("password");
                System.out.println(email + "|" + password);
            }

        } catch (SQLException e) {
            System.out.println("Error connecting to Database");
            e.printStackTrace();
        }
    }


}
