import java.io.*;
import java.net.Socket;
import java.sql.*;

public class ServerThread extends Thread {

    static final String DB_URL = "jdbc:sqlite:marketplacedb.db";
    Socket socket;

    public ServerThread(Socket client) {
        this.socket = client;
    }

    public void run() {
        BufferedReader reader = null;
        PrintWriter writer = null;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            System.out.println("Error Connecting to Database");
            e.printStackTrace();
        }

        String clientInput = null;
        Statement statement = null;
        while (true) {
            try {
                clientInput = reader.readLine();
                String[] inputList = clientInput.split(" ");
                String command = inputList[0];
                int execute = 0;
//                execute == 1 if executeUpdate, execute == 2 if executeQuery, execute == 0 to quit
                boolean isAppointment = clientInput.toLowerCase().contains("appointments");
                switch (command) {
                    case "SELECT":
                        execute = 2;
                        break;
                    case "UPDATE":
                    case "INSERT":
                    case "DELETE":
                        execute = 1;
                        break;
                    case "QUIT":
                    default:
                        break;
                }


                statement = connection.createStatement();


            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Could not pass data to / receive data from client");
                return;
            } catch (SQLException e) {
                System.out.println("Statement issue. Statement was: " + statement);
                e.printStackTrace();
                return;
            }
        }
    }
}
