import javax.swing.*;
import java.sql.*;
public class TempSeller {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:sqlite:/C:\\Users\\takoy\\IdeaProjects\\Project_5\\marketplacedb.db";
        try {
            Connection connection = DriverManager.getConnection(jdbcUrl);
            String sql = "SELECT * FROM users";

            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                String name = result.getString("name");
                String email = result.getString("email");
                System.out.println(name + " | " + email);
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to SQLite database");
            e.printStackTrace();
        }
        String[] selOp = new String[] {
                "View Approved", "Appointment Requests",
                "Create Store", "Create Calendar",
                "Edit Calendar", "Delete Calendar",
                "Show Statistics", "Import Calendar",
                "Logout", "Quit"
        };
        String choice = (String) JOptionPane.showInputDialog(null, "Select an operation:",
                "Calendar", JOptionPane.PLAIN_MESSAGE, null, selOp, null);
    }
}
